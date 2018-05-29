package fr.gouv.education.sirhen.ct.moteurregles.service.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import fr.gouv.education.sirhen.ct.moteurregles.commun.IRegle;
import fr.gouv.education.sirhen.ct.moteurregles.commun.impl.Regle;
import fr.gouv.education.sirhen.ct.moteurregles.transverse.Constantes;
import fr.gouv.education.sirhen.ct.socle.configuration.ConfigurationComposantTechnique;

public class GPMoteurReglesServiceImpl extends MoteurReglesServiceImpl {

	private final static String NOM_ANNOTATION_CODE_ONP = "codeonp";
	private final static String NOM_ANNOTATION_NOYAU_ONP = "noyauonp";
	private final static String NOM_ANNOTATION_EVENEMENTS = "evenements";
	private final static String NOM_ANNOTATION_DATE_DEBUT_VALIDITE = "datedebutvalidite";
	private final static String NOM_ANNOTATION_DATE_FIN_VALIDITE = "datefinvalidite";
	private final static String NOM_ANNOTATION_POPULATIONS = "populations";
	private final static String NOM_ANNOTATION_BLOQUANTE = "bloquante";
	private final static String NOM_ANNOTATION_IGNORE = "ignore";
	private final static String NOM_ANNOTATION_LIBELLE = "libelle";
	private final static String NOM_ANNOTATION_COMMENTAIRE = "commentaire";

	private static final String CLASSPATH_RULES_DRL = "classpath*:**/rules/**/*.drl";

	protected KieContainer kieContainer;

	private KieServices kieServices = KieServices.Factory.get();

	public GPMoteurReglesServiceImpl(final ApplicationContext applicationContext,
		final ConfigurationComposantTechnique configuration) {
		super(applicationContext, configuration);
	}

	@Override
	public void initialiseBaseConnaissance() {
		if (kContainer != null) {
			return;
		} else {
			kieServices = KieServices.Factory.get();
			kContainer = getKieContainer();
		}

		if (kContainer == null) {
			factory.throwTechnicalException(Constantes.ERR_CREATION_KB);
		}
	}

	private void getKieRepository() {
		final KieRepository kieRepository = kieServices.getRepository();
		kieRepository.addKieModule(new KieModule() {
			public ReleaseId getReleaseId() {
				return kieRepository.getDefaultReleaseId();
			}
		});
	}

	public KieContainer getKieContainer() {
		getKieRepository();
		Resource[] fichiersRegles = getFichiersRegles();

		analyseFichiersRegles(fichiersRegles);

		KieBuilder kb;
		try {
			kb = kieServices.newKieBuilder(kieFileSystem(fichiersRegles));
		} catch (IOException e) {
			return null;
		}
		kb.buildAll();

		KieModule kieModule = kb.getKieModule();
		KieContainer kContainer = kieServices.newKieContainer(kieModule.getReleaseId());

		return kContainer;
	}

	private List < IRegle > analyseFichiersRegles(final Resource[] fichiersRegles) {

		List < IRegle > resultat = new ArrayList <>();
		for (Resource fichierRegles : fichiersRegles) {
			try {
				String ruleContent = IOUtils.toString(fichierRegles.getInputStream(), StandardCharsets.UTF_8.name());
				DrlParser parser = new DrlParser();
				PackageDescr pkgDescr = null;
				try {
					pkgDescr = parser.parse(null, ruleContent);
					if (pkgDescr != null) {
						// La règle est syntaxiquement correcte
						List < RuleDescr > regles = pkgDescr.getRules();
						for (RuleDescr descripteur : regles) {
							Regle regle = new Regle();
							CaseInsensitiveList annotations = new CaseInsensitiveList();
							annotations.addAll(descripteur.getAnnotationNames());

							if (annotations.contains(NOM_ANNOTATION_IGNORE)) {
								// L'annotation @Ignore est présente, la règle est ignorée
								continue;
							}

							if (annotations.contains(NOM_ANNOTATION_CODE_ONP)) {
								regle.setCode(descripteur.getAnnotation(annotations.getOriginalValue(NOM_ANNOTATION_CODE_ONP))
									.getSingleValue());
							}

							if (annotations.contains(NOM_ANNOTATION_LIBELLE)) {
								regle.setLibelle(descripteur.getAnnotation(annotations.getOriginalValue(NOM_ANNOTATION_LIBELLE))
									.getSingleValue());
							}

							if (annotations.contains(NOM_ANNOTATION_LIBELLE)) {
								regle.setLibelle(descripteur.getAnnotation(annotations.getOriginalValue(NOM_ANNOTATION_LIBELLE))
									.getSingleValue());
							}

							regle.setBloquante(annotations.contains(NOM_ANNOTATION_BLOQUANTE));

							if (annotations.contains(NOM_ANNOTATION_EVENEMENTS)) {
								String aux = descripteur.getAnnotation(annotations.getOriginalValue(NOM_ANNOTATION_EVENEMENTS))
									.getSingleValue();
								String[] split = aux.split(",");
								regle.setEvenements(Arrays.asList(split));
							} else {
								regle.setEvenements(new ArrayList <>());
							}

							resultat.add(regle);
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

	public KieFileSystem kieFileSystem(final Resource[] fichiersRegles) throws IOException {
		KieFileSystem kieFileSystem = kieServices.newKieFileSystem();

		for (Resource fichierRegles : fichiersRegles) {
			kieFileSystem.write(ResourceFactory.newFileResource(fichierRegles.getFile()));
		}

		return kieFileSystem;
	}

}
