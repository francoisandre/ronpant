package fr.gouv.education.sirhen.ct.moteurregles.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.kie.api.KieServices;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import fr.gouv.education.sirhen.ct.commun.transverse.exception.TechniqueExceptionFactory;
import fr.gouv.education.sirhen.ct.moteurregles.commun.IFait;
import fr.gouv.education.sirhen.ct.moteurregles.commun.IRegle;
import fr.gouv.education.sirhen.ct.moteurregles.commun.impl.Regle;
import fr.gouv.education.sirhen.ct.moteurregles.commun.impl.Resultat;
import fr.gouv.education.sirhen.ct.moteurregles.filter.ReglesAgendaFilter;
import fr.gouv.education.sirhen.ct.moteurregles.service.IMoteurReglesService;
import fr.gouv.education.sirhen.ct.moteurregles.transverse.Constantes;
import fr.gouv.education.sirhen.ct.moteurregles.transverse.util.MoteurReglesUtil;
import fr.gouv.education.sirhen.ct.moteurregles.transverse.vo.TypeResultat;
import fr.gouv.education.sirhen.ct.socle.configuration.ConfigurationComposantTechnique;

/**
 *
 * Moteur de règles.
 *
 */
public class MoteurReglesServiceImpl implements IMoteurReglesService {

	/** Logger de la classe MoteurRegles. */
	private static Logger logger = LoggerFactory.getLogger(MoteurReglesServiceImpl.class);
	/** Factory d'exceptions techniques. */
	protected static TechniqueExceptionFactory factory = TechniqueExceptionFactory.getInstance(MoteurReglesServiceImpl.class);
	/** Base de connaissance de JBoss Rules. */
	protected KieContainer kContainer;
	/** lien vers le kjar dans le filesystem */
	private String kjarfile;
	/** Liste des fichiers xml de définition des règles automatisables hors BRMS. */
	private String xmlruleslist;
	/** Liste des ressources xmlrules automatisables. */
	private Resource[] xmlrules;
	/** Liste des définitions de règles automatisables. */
	private List < IRegle > listReglesFromXml;
	/** configuration du composant technique. */
	private Properties properties;
	/** Application Context. */
	private ApplicationContext applicationContext;

	/**
	 * Constructeur.
	 *
	 * @param applicationContext
	 *            application contexte.
	 * @param configuration
	 *            configuration du composant technique.
	 */
	public MoteurReglesServiceImpl(final ApplicationContext applicationContext,
		final ConfigurationComposantTechnique configuration) {
		super();
		this.applicationContext = applicationContext;
		setParametres(configuration);
		initialiseBaseConnaissance();
		getListeRegles();
	}

	/**
	 * Instancie la base de connaissance.
	 *
	 * @throws Exception
	 */
	public void initialiseBaseConnaissance() {

		if (kContainer != null) {
			return;
		}
		KieServices ks = KieServices.Factory.get();
		KieRepository kr = ks.getRepository();
		KieModule kModule = kr.addKieModule(ks.getResources().newFileSystemResource(kjarfile));
		kContainer = ks.newKieContainer(kModule.getReleaseId());
		if (kContainer == null) {
			factory.throwTechnicalException(Constantes.ERR_CREATION_KB);
		}

	}

	/**
	 * Appel pour exècuter les règles correspondant à un type d'évèmenement en mode statefull/stateless. Les règles exécutées sont
	 * retournées.
	 *
	 * @param faits
	 *            La liste de faits etat de la base de règles
	 * @param typeEvenement
	 *            Le type d'évènement.
	 * @return résultat de l'exécution.
	 */
	public Resultat executerRegles(final Set < IFait > faits, final String typeEvenement) {
		Resultat ret = new Resultat();
		// On rajoute les règles au faits
		final List < IRegle > regles = getListeRegles(typeEvenement);
		for (IRegle regle : regles) {
			faits.add((Regle) regle);
		}
		// On rajoute l'objet résultat au fait
		faits.add(ret);
		// session pour l'execution des règles
		KieSession kSession = kContainer.newKieSession();
		// kSession.setGlobal("logger", logger);
		for (Object fact : faits) {
			kSession.insert(fact);
		}
		if (typeEvenement == null) {
			kSession.fireAllRules();
		} else {
			kSession.fireAllRules(new ReglesAgendaFilter(typeEvenement));
		}

		// On filtre les regles par population.
		for (IRegle iRegle : regles) {
			if (iRegle.isAppartientPopulation(ret.getPopulation()) && !iRegle.getCode().contains(Constantes.REGLE_INTERNE)) {
				ret.getRegles().add(iRegle);
			}
		}
		// libére la session
		kSession.dispose();
		return ret;
	}

	/**
	 * Remonte la liste des règles en partant du fichier XML de la définition des règles.
	 *
	 * @return la liste de règles
	 */
	public List < IRegle > getListeRegles() {
		return getListeRegles(null);
	}

	/**
	 * Remonte la liste des règles applicable au type d'évènement passé en paramètre.
	 *
	 * @param typeEvenement
	 *            type d'évènement.
	 * @return la liste de règles.
	 */
	@SuppressWarnings("unchecked")
	public final List < IRegle > getListeRegles(final String typeEvenement) {
		if (CollectionUtils.isEmpty(listReglesFromXml)) {
			try {
				listReglesFromXml = new ArrayList < IRegle >();
				// READ FROM JAR
				for (String path : xmlruleslist.split(",")) {

					JarFile jar = new JarFile(kjarfile);
					try {
						Enumeration < JarEntry > entries = jar.entries();
						while (entries.hasMoreElements()) {
							JarEntry entry = entries.nextElement();
							if (!entry.isDirectory() && entry.getName().endsWith(path)) {
								InputStream in = jar.getInputStream(entry);
								try {
									List < IRegle > listRegles = MoteurReglesUtil.lireFichierRegles(in);
									listReglesFromXml.addAll(listRegles);
								} finally {
									in.close();
								}
							}
						}
					} finally {
						jar.close();
					}
				}
				// READ FROM JAR

			} catch (IOException e) {
				factory.throwTechnicalException(Constantes.ERR_ACCES_XML_REGLES_BRMS, e);
			}
		}

		List < IRegle > reglesList = new ArrayList < IRegle >();
		if (typeEvenement == null) {
			reglesList = listReglesFromXml;
		} else {
			reglesList = (List < IRegle >) CollectionUtils.select(listReglesFromXml, new Predicate() {
				@Override
				public boolean evaluate(final Object object) {
					return ((Regle) object).isAppartientEvenement(typeEvenement);
				}
			});
		}
		return MoteurReglesUtil.clonerRegles(reglesList);
	}

	/**
	 * Extraire les informations des attributs.
	 */
	public final void splitlists() {
		if (this.xmlrules == null && xmlruleslist != null && xmlruleslist.length() > 0) {
			String[] r = xmlruleslist.split(",");
			List < Resource > resources = new ArrayList < Resource >(r.length);
			for (String path : r) {
				Resource resource = applicationContext.getResource(path);
				if (!resource.exists()) {
					factory.throwTechnicalException(Constantes.ERR_CHARGEMENT_FICHIER, path);
				}
				resources.add(resource);
			}
			this.xmlrules = resources.toArray(new Resource[] {});
		}
	}

	/**
	 * Initialise les attributs de la factory.
	 *
	 * @param configuration
	 *            configuration du moteur.
	 */
	public final void setParametres(final ConfigurationComposantTechnique configuration) {
		this.properties = configuration.getProperties();
		this.kjarfile = configuration.get(Constantes.CLE_KJAR_FILE);
		if (StringUtils.isBlank(this.kjarfile)) {
			factory.throwTechnicalException(Constantes.ERR_CLES_OBLIGATOIRES, Constantes.CLE_KJAR_FILE);
		}
		this.xmlruleslist = configuration.get(Constantes.CLE_FICHIER_XML_REGLES_LOCALES);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setTypeResultat(final TypeResultat type) {
		return;
	}
}
