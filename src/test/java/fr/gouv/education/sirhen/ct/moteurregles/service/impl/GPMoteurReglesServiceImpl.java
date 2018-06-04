package fr.gouv.education.sirhen.ct.moteurregles.service.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.io.IOUtils;
import org.drools.compiler.compiler.DrlParser;
import org.drools.compiler.compiler.DroolsParserException;
import org.drools.compiler.lang.descr.PackageDescr;
import org.drools.compiler.lang.descr.RuleDescr;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import fr.gouv.education.sirhen.ct.commun.transverse.exception.TechniqueExceptionFactory;
import fr.gouv.education.sirhen.ct.moteurregles.commun.IFait;
import fr.gouv.education.sirhen.ct.moteurregles.commun.impl.Regle;
import fr.gouv.education.sirhen.ct.moteurregles.transverse.Constantes;
import fr.gouv.education.sirhen.gin.gestiondescontratsdesnontitulaires.fait.IFournisseurJourExecution;
import fr.gouv.education.sirhen.gin.gestiondescontratsdesnontitulaires.fait.IFournisseurPopulation;

public class GPMoteurReglesServiceImpl implements IGPAnnotations {

	private static final String CLASSPATH_RULES_DRL = "classpath*:**/rules/**/*.drl";

	protected KieContainer kieContainer;

	private KieServices kieServices;
	private Map < String, List < IGPRegle > > tableEvenements;
	protected static TechniqueExceptionFactory factory = TechniqueExceptionFactory.getInstance(MoteurReglesServiceImpl.class);

	public GPMoteurReglesServiceImpl() throws Exception {
		kieServices = KieServices.Factory.get();
		getKieRepository();
		if (kieServices == null) {
			factory.throwTechnicalException(Constantes.ERR_CREATION_KB);
		}

		Resource[] fichiersRegles = getFichiersRegles();
		tableEvenements = analyseFichiersRegles(fichiersRegles);

	}

	private void getKieRepository() {
		final KieRepository kieRepository = kieServices.getRepository();
		kieRepository.addKieModule(new KieModule() {
			public ReleaseId getReleaseId() {
				return kieRepository.getDefaultReleaseId();
			}
		});
	}

	public KieContainer getKieContainer(final List < IGPRegle > regles) {

		KieBuilder kb;
		try {
			kb = kieServices.newKieBuilder(kieFileSystem(regles));
		} catch (IOException e) {
			return null;
		}
		kb.buildAll();

		KieModule kieModule = kb.getKieModule();
		KieContainer kContainer = kieServices.newKieContainer(kieModule.getReleaseId());

		return kContainer;
	}

	private Map < String, List < IGPRegle > > analyseFichiersRegles(final Resource[] fichiersRegles) throws Exception {

		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		Map < String, List < IGPRegle > > resultat = new HashMap <>();

		for (Resource fichierRegles : fichiersRegles) {
			try {
				String ruleContent = IOUtils.toString(fichierRegles.getInputStream(), StandardCharsets.UTF_8.name());
				DrlParser parser = new DrlParser();
				PackageDescr pkgDescr = null;
				try {
					pkgDescr = parser.parse(null, ruleContent);
					if (pkgDescr == null) {
						throw new Exception("Le fichier " + fichierRegles.getFilename() + " est syntaxiquement incorrect");
					}
					if (pkgDescr != null) {
						// La règle est syntaxiquement correcte
						List < RuleDescr > regles = pkgDescr.getRules();
						for (RuleDescr descripteur : regles) {

							Regle regle = new Regle();

							Set < String > annotationNames = descripteur.getAnnotationNames();
							Map < String, String > annotations = new CaseInsensitiveMap();
							for (String annotation : annotationNames) {
								annotations.put(annotation, annotation);
							}

							if (annotations.containsKey(NOM_ANNOTATION_IGNORE)) {
								// L'annotation @Ignore est présente, la règle est ignorée
								continue;
							}

							if (annotations.containsKey(NOM_ANNOTATION_EVENEMENTS)) {
								String aux = descripteur.getAnnotation(annotations.get(NOM_ANNOTATION_EVENEMENTS))
									.getSingleValue();
								String[] split = aux.split(",");
								regle.setEvenements(Arrays.asList(split));
							} else {
								// Une règle sans évènements doit être ignorée
								continue;
							}

							if (annotations.containsKey(NOM_ANNOTATION_CODE_ONP)) {
								regle.setCode(
									descripteur.getAnnotation(annotations.get(NOM_ANNOTATION_CODE_ONP)).getSingleValue());
							}

							if (annotations.containsKey(NOM_ANNOTATION_LIBELLE)) {
								regle.setLibelle(
									descripteur.getAnnotation(annotations.get(NOM_ANNOTATION_LIBELLE)).getSingleValue());
							}

							regle.setBloquante(annotations.containsKey(NOM_ANNOTATION_BLOQUANTE));

							if (annotations.containsKey(NOM_ANNOTATION_DATE_DEBUT_VALIDITE)) {
								String aux = descripteur.getAnnotation(annotations.get(NOM_ANNOTATION_DATE_DEBUT_VALIDITE))
									.getSingleValue();
								try {
									Date date = format.parse(aux);
									regle.setDateDebutApplication(date);
								} catch (ParseException e) {
									// La date est ignorée
								}
							}

							if (annotations.containsKey(NOM_ANNOTATION_DATE_FIN_VALIDITE)) {
								String aux = descripteur.getAnnotation(annotations.get(NOM_ANNOTATION_DATE_FIN_VALIDITE))
									.getSingleValue();
								try {
									Date date = format.parse(aux);
									regle.setDateFinApplication(date);
								} catch (ParseException e) {
									// La date est ignorée
								}
							}

							if (annotations.containsKey(NOM_ANNOTATION_POPULATIONS)) {
								String aux = descripteur.getAnnotation(annotations.get(NOM_ANNOTATION_POPULATIONS))
									.getSingleValue();
								String[] split = aux.split(",");
								regle.setPopulations(Arrays.asList(split));
							} else {
								regle.setPopulations(new ArrayList <>());
							}

							for (String evenement : regle.getEvenements()) {
								String evt = evenement.toLowerCase();
								if (!resultat.containsKey(evt)) {
									resultat.put(evt, new ArrayList <>());
								}
								List < IGPRegle > aux = resultat.get(evt);

								aux.add(new RegleFichier(regle, fichierRegles));
							}
						}
					}

				} catch (DroolsParserException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				continue;
			}
		}

		return resultat;

	}

	public Resource[] getFichiersRegles() {
		ClassLoader cl = Thread.currentThread().getContextClassLoader().getClass().getClassLoader();
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
		Resource[] resources = new Resource[0];
		try {
			resources = resolver.getResources(CLASSPATH_RULES_DRL);
		} catch (IOException e) {
			return new Resource[0];
		}
		return resources;
	}

	public KieFileSystem kieFileSystem(final List < IGPRegle > regles) throws IOException {
		KieFileSystem kieFileSystem = kieServices.newKieFileSystem();

		for (IGPRegle regle : regles) {
			kieFileSystem.write(ResourceFactory.newFileResource(regle.getResource().getFile()));
		}

		return kieFileSystem;
	}

	public KieSession retourneSessionPourRegles(final String evenement, final String population, final Date jourExecution,
		final GPResultat resultat) {
		// On détermine les règles correspondant à l'évènement
		final List < IGPRegle > regles = tableEvenements.get(evenement.toLowerCase());
		final List < IGPRegle > reglesAVerifier = filtreReglesAVerifier(regles, population, jourExecution, resultat);
		KieSession session = null;
		KieContainer container = getKieContainer(reglesAVerifier);
		session = container.newKieSession();
		session.addEventListener(new GPAgendaEventListener(resultat));
		return session;
	}

	private List < IGPRegle > filtreReglesAVerifier(final List < IGPRegle > regles, final String population,
		final Date jourExecution, final GPResultat resultat) {
		List < IGPRegle > reglesAVerifier = new ArrayList <>();
		for (IGPRegle regle : regles) {
			boolean ignoree = false;
			if (!regle.isAppartientPopulation(population)) {
				resultat.ajouteExecution(new ExecutionRegle(ExecutionRegle.REGLE_IGNOREE, regle,
					"Règle ignorée car ne concernant pas la population " + population + "."));
				ignoree = true;
			}

			if (regle.getDateDebutApplication() != null) {
				if (jourExecution.before(regle.getDateDebutApplication().getTime())) {
					resultat.ajouteExecution(new ExecutionRegle(ExecutionRegle.REGLE_IGNOREE, regle,
						"Règle ignorée car la date d'exécution est antérieure à la date d'application. " + population));
					ignoree = true;
				}
			}

			if (regle.getDateFinApplication() != null) {
				if (jourExecution.after(regle.getDateFinApplication().getTime())) {
					resultat.ajouteExecution(new ExecutionRegle(ExecutionRegle.REGLE_IGNOREE, regle,
						"Règle ignorée car la date d'exécution est postérieure à la date d'application. " + population));
					ignoree = true;
				}
			}

			if (ignoree == false) {
				// Par défaut une règle est considérée comme non vérifiée
				resultat.ajouteExecution(new ExecutionRegle(ExecutionRegle.REGLE_NON_VERIFIEE, regle, regle.getLibelle()));
				reglesAVerifier.add(regle);
			}

		}
		return reglesAVerifier;
	}

	public final GPResultat executerReglesGP(final Set < IFait > faits, final String evenement) {
		GPResultat resultat = new GPResultat();
		String population = getPopulation(faits);
		Date jourExecution = getJourExecution(faits);
		KieSession kSession = retourneSessionPourRegles(evenement, population, jourExecution, resultat);

		for (IFait fait : faits) {
			kSession.insert(fait);
		}
		kSession.fireAllRules();
		kSession.dispose();
		return resultat;
	}

	private String getPopulation(final Set < IFait > faits) {
		for (IFait fait : faits) {
			if (fait instanceof IFournisseurPopulation) {
				return ((IFournisseurPopulation) fait).getCodePopulation();
			}
		}
		return IFournisseurPopulation.NO_POPULATION;
	}

	private Date getJourExecution(final Set < IFait > faits) {
		for (IFait fait : faits) {
			if (fait instanceof IFournisseurJourExecution) {
				return ((IFournisseurJourExecution) fait).getJourExecution();
			}
		}

		Calendar calendrier = Calendar.getInstance();
		return calendrier.getTime();
	}

}
