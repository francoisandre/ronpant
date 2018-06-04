package fr.gouv.education.sirhen.gin.gestiondescontratsdesnontitulaires.fait;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.gouv.education.sirhen.ct.moteurregles.commun.IFait;
import fr.gouv.education.sirhen.ct.moteurregles.service.impl.GPMoteurReglesServiceImpl;
import fr.gouv.education.sirhen.ct.moteurregles.service.impl.GPResultat;
import fr.gouv.education.sirhen.gin.gestiondescontratsdesnontitulaires.evenement.Constantes;

@ContextConfiguration(classes = TestConfiguration.class)
@RunWith(value = SpringJUnit4ClassRunner.class)
public class TestCreationContrat extends AbstractTestRegles {

	@Autowired
	GPMoteurReglesServiceImpl moteurRegle;

	/**
	 * Test la règle : La date de début du contrat est postérieure ou égale à la date d'entrée dans la FPE ou dans la carrière
	 * militaire.
	 *
	 * @throws Exception
	 */
	@Test
	public void testRegleRAN_C_001() throws Exception {

		// On fait les IFait nécessaires
		AgentFait agent = new AgentFait();
		agent.setDateEntreeFonctionPublique(parseDate("01/09/2017"));
		agent.setDateLimiteRetraite(parseDate("01/09/2035"));
		agent.setCodePopulation(IPopulations.MAITRE_DU_PRIVE);

		ContratFait contrat = new ContratFait();
		contrat.setDateDebutLienJuridique(parseDate("01/09/2017"));
		contrat.setDateFinPrevisionelleLienJuridique(parseDate("31/08/2018"));
		contrat.setTypeContrat("CDD");
		contrat.setIdContrat("DUPOND20180016");
		contrat.setTypeLienJuridique("01");

		Set < IFait > faits = new HashSet <>();
		faits.add(agent);
		faits.add(contrat);

		GPResultat resultatExecution = moteurRegle.executerReglesGP(faits, Constantes.EVENEMENT_CREATION_CONTRAT);
		Assert.assertTrue("La règle RAN_C_001 doit être vérifiée", regleEstVerifiee("RAN_C_001", resultatExecution));

		AgentFait autreAgent = new AgentFait();
		autreAgent.setDateEntreeFonctionPublique(parseDate("02/09/2017"));
		autreAgent.setDateLimiteRetraite(parseDate("01/09/2035"));
		autreAgent.setCodePopulation(IPopulations.MAITRE_DU_PRIVE);
		faits = new HashSet <>();
		faits.add(autreAgent);
		faits.add(contrat);

		resultatExecution = moteurRegle.executerReglesGP(faits, Constantes.EVENEMENT_CREATION_CONTRAT);
		Assert.assertTrue("La règle RAN_C_001 doit être non vérifiée", regleEstNonVerifiee("RAN_C_001", resultatExecution));

		AgentFait encoreUnautreAgent = new AgentFait();
		encoreUnautreAgent.setDateLimiteRetraite(parseDate("01/09/2035"));
		encoreUnautreAgent.setCodePopulation(IPopulations.MAITRE_DU_PRIVE);
		faits = new HashSet <>();
		faits.add(encoreUnautreAgent);
		faits.add(contrat);

		resultatExecution = moteurRegle.executerReglesGP(faits, Constantes.EVENEMENT_CREATION_CONTRAT);
		Assert.assertTrue("La règle RAN_C_001 doit être non vérifiée", regleEstNonVerifiee("RAN_C_001", resultatExecution));

	}

	/**
	 * Test la règle : La date de fin prévisionnelle du contrat est postérieure ou égale à la date de début du contrat
	 *
	 * @throws Exception
	 */
	@Test
	public void testRegleRAN_C_005() throws Exception {
		// On fait les IFait nécessaires
		AgentFait agent = new AgentFait();
		agent.setDateEntreeFonctionPublique(parseDate("01/09/2017"));
		agent.setDateLimiteRetraite(parseDate("01/09/2035"));
		agent.setCodePopulation(IPopulations.MAITRE_DU_PRIVE);

		ContratFait contrat = new ContratFait();
		contrat.setDateDebutLienJuridique(parseDate("01/09/2017"));
		contrat.setDateFinPrevisionelleLienJuridique(parseDate("31/08/2018"));
		contrat.setTypeContrat("CDD");
		contrat.setIdContrat("DUPOND20180016");
		contrat.setTypeLienJuridique("01");

		Set < IFait > faits = new HashSet <>();
		faits.add(agent);
		faits.add(contrat);

		GPResultat resultatExecution = moteurRegle.executerReglesGP(faits, Constantes.EVENEMENT_CREATION_CONTRAT);
		Assert.assertTrue("La règle RAN_C_005 doit être vérifiée", regleEstVerifiee("RAN_C_005", resultatExecution));

		ContratFait autreContrat = new ContratFait();
		autreContrat.setDateDebutLienJuridique(parseDate("01/09/2017"));
		autreContrat.setDateFinPrevisionelleLienJuridique(parseDate("31/08/2017"));
		autreContrat.setTypeContrat("CDD");
		autreContrat.setIdContrat("DUPOND20180016");
		autreContrat.setTypeLienJuridique("01");
		faits = new HashSet <>();
		faits.add(agent);
		faits.add(autreContrat);

		resultatExecution = moteurRegle.executerReglesGP(faits, Constantes.EVENEMENT_CREATION_CONTRAT);
		Assert.assertFalse("La règle RAN_C_005 doit être non vérifiée", regleEstVerifiee("RAN_C_005", resultatExecution));
	}

	/**
	 * Test la règle : La date de fin réelle du contrat est postérieure ou égale à la date de début du contrat
	 *
	 * @throws Exception
	 */
	@Test
	public void testRegleRAN_C_105() throws Exception {
		// On fait les IFait nécessaires
		AgentFait agent = new AgentFait();
		agent.setDateEntreeFonctionPublique(parseDate("01/09/2017"));
		agent.setDateLimiteRetraite(parseDate("01/09/2035"));
		agent.setCodePopulation(IPopulations.MAITRE_DU_PRIVE);

		ContratFait contrat = new ContratFait();
		contrat.setDateDebutLienJuridique(parseDate("01/09/2017"));
		contrat.setDateFinPrevisionelleLienJuridique(parseDate("31/08/2018"));
		contrat.setDateFinReelLienJuridique(parseDate("31/08/2018"));
		contrat.setTypeContrat("CDD");
		contrat.setIdContrat("DUPOND20180016");
		contrat.setTypeLienJuridique("01");

		Set < IFait > faits = new HashSet <>();
		faits.add(agent);
		faits.add(contrat);

		GPResultat resultatExecution = moteurRegle.executerReglesGP(faits, Constantes.EVENEMENT_CREATION_CONTRAT);
		Assert.assertTrue("La règle RAN_C_105 doit être vérifiée", regleEstVerifiee("RAN_C_105", resultatExecution));

		ContratFait autreContrat = new ContratFait();
		autreContrat.setDateDebutLienJuridique(parseDate("01/09/2017"));
		autreContrat.setDateFinPrevisionelleLienJuridique(parseDate("31/09/2017"));
		autreContrat.setDateFinReelLienJuridique(parseDate("30/08/2017"));
		autreContrat.setTypeContrat("CDD");
		autreContrat.setIdContrat("DUPOND20180016");
		autreContrat.setTypeLienJuridique("01");

		faits = new HashSet <>();
		faits.add(agent);
		faits.add(autreContrat);

		resultatExecution = moteurRegle.executerReglesGP(faits, Constantes.EVENEMENT_CREATION_CONTRAT);
		Assert.assertFalse("La règle RAN_C_105 doit être non vérifiée", regleEstVerifiee("RAN_C_105", resultatExecution));
	}

	/**
	 * test la règle : La date de fin prévisionnelle du contrat est inférieure à la date limite de départ à la retraite.
	 *
	 * @throws Exception
	 */
	@Test
	public void testRegleRAN_C_024() throws Exception {
		// On fait les IFait nécessaires
		AgentFait agent = new AgentFait();
		agent.setDateEntreeFonctionPublique(parseDate("01/09/2017"));
		agent.setDateLimiteRetraite(parseDate("01/09/2035"));
		agent.setCodePopulation(IPopulations.MAITRE_DU_PRIVE);

		ContratFait contrat = new ContratFait();
		contrat.setDateDebutLienJuridique(parseDate("01/09/2017"));
		contrat.setDateFinPrevisionelleLienJuridique(parseDate("31/08/2018"));
		contrat.setDateFinReelLienJuridique(parseDate("31/08/2018"));
		contrat.setTypeContrat("CDD");
		contrat.setIdContrat("DUPOND20180016");
		contrat.setTypeLienJuridique("01");

		Set < IFait > faits = new HashSet <>();
		faits.add(agent);
		faits.add(contrat);

		GPResultat resultatExecution = moteurRegle.executerReglesGP(faits, Constantes.EVENEMENT_CREATION_CONTRAT);
		Assert.assertTrue("La règle RAN_C_024 doit être vérifiée", regleEstVerifiee("RAN_C_024", resultatExecution));

		ContratFait autreContrat = new ContratFait();
		autreContrat.setDateDebutLienJuridique(parseDate("01/09/2017"));
		autreContrat.setDateFinPrevisionelleLienJuridique(parseDate("02/09/2035"));
		autreContrat.setTypeContrat("CDD");
		autreContrat.setIdContrat("DUPOND20180016");
		autreContrat.setTypeLienJuridique("01");
		faits = new HashSet <>();
		faits.add(agent);
		faits.add(autreContrat);

		resultatExecution = moteurRegle.executerReglesGP(faits, Constantes.EVENEMENT_CREATION_CONTRAT);
		Assert.assertFalse("La règle RAN_C_024 doit être non vérifiée", regleEstVerifiee("RAN_C_024", resultatExecution));
	}

	/**
	 * Test la règle : La date de fin réelle du contrat est inférieure à la date limite de départ à la retraite.
	 *
	 * @throws Exception
	 */
	@Test
	public void testRegleRAN_C_124() throws Exception {
		// On fait les IFait nécessaires
		AgentFait agent = new AgentFait();
		agent.setDateEntreeFonctionPublique(parseDate("01/09/2017"));
		agent.setDateLimiteRetraite(parseDate("15/09/2018"));
		agent.setCodePopulation(IPopulations.MAITRE_DU_PRIVE);

		ContratFait contrat = new ContratFait();
		contrat.setDateDebutLienJuridique(parseDate("01/09/2017"));
		contrat.setDateFinPrevisionelleLienJuridique(parseDate("31/08/2018"));
		contrat.setDateFinReelLienJuridique(parseDate("31/08/2018"));
		contrat.setTypeContrat("CDD");
		contrat.setIdContrat("DUPOND20180016");
		contrat.setTypeLienJuridique("01");

		Set < IFait > faits = new HashSet <>();
		faits.add(agent);
		faits.add(contrat);

		GPResultat resultatExecution = moteurRegle.executerReglesGP(faits, Constantes.EVENEMENT_CREATION_CONTRAT);
		Assert.assertTrue("La règle RAN_C_124 doit être vérifiée", regleEstVerifiee("RAN_C_124", resultatExecution));

		ContratFait autreContrat = new ContratFait();
		autreContrat.setDateDebutLienJuridique(parseDate("01/09/2017"));
		autreContrat.setDateFinPrevisionelleLienJuridique(parseDate("31/08/2018"));
		autreContrat.setDateFinReelLienJuridique(parseDate("16/09/2018"));
		autreContrat.setTypeContrat("CDD");
		autreContrat.setIdContrat("DUPOND20180016");
		autreContrat.setTypeLienJuridique("01");
		faits = new HashSet <>();
		faits.add(agent);
		faits.add(autreContrat);

		resultatExecution = moteurRegle.executerReglesGP(faits, Constantes.EVENEMENT_CREATION_CONTRAT);
		Assert.assertFalse("La règle RAN_C_124 doit être non vérifiée", regleEstVerifiee("RAN_C_124", resultatExecution));
	}

	/**
	 * Test la règle : Les éléments suivants : Identifiant contrat, Date début du lien juridique, Type de contrat et Lien
	 * juridique doivent être renseignés dans le contrat de l'agent.
	 *
	 * @throws Exception
	 */
	@Test
	public void testRegleRAN_C_927() throws Exception {
		// On fait les IFait nécessaires
		ContratFait contrat = new ContratFait();
		contrat.setDateDebutLienJuridique(parseDate("01/09/2017"));
		contrat.setDateFinPrevisionelleLienJuridique(parseDate("31/08/2018"));
		contrat.setTypeContrat("CDD");
		contrat.setIdContrat("DUPOND20180016");
		contrat.setTypeLienJuridique("01");

		Set < IFait > faits = new HashSet <>();
		faits.add(contrat);

		GPResultat resultatExecution = moteurRegle.executerReglesGP(faits, Constantes.EVENEMENT_CREATION_CONTRAT);
		Assert.assertTrue("La règle RAN_C_927 doit être vérifiée", regleEstVerifiee("RAN_C_927", resultatExecution));

		ContratFait autreContrat = new ContratFait();
		autreContrat.setDateDebutLienJuridique(parseDate("01/09/2017"));
		autreContrat.setDateFinPrevisionelleLienJuridique(parseDate("31/08/2018"));
		autreContrat.setTypeContrat("CDD");
		autreContrat.setTypeLienJuridique("01");
		faits = new HashSet <>();
		faits.add(autreContrat);

		resultatExecution = moteurRegle.executerReglesGP(faits, Constantes.EVENEMENT_CREATION_CONTRAT);
		Assert.assertFalse("La règle RAN_C_927 doit être non vérifiée", regleEstVerifiee("RAN_C_927", resultatExecution));

		ContratFait encoreUnAutreContrat = new ContratFait();
		encoreUnAutreContrat.setDateFinPrevisionelleLienJuridique(parseDate("31/08/2018"));
		encoreUnAutreContrat.setTypeContrat("CDD");
		encoreUnAutreContrat.setIdContrat("DUPOND20180016");
		encoreUnAutreContrat.setTypeLienJuridique("01");
		faits = new HashSet <>();
		faits.add(encoreUnAutreContrat);

		resultatExecution = moteurRegle.executerReglesGP(faits, Constantes.EVENEMENT_CREATION_CONTRAT);
		Assert.assertFalse("La règle RAN_C_927 doit être non vérifiée", regleEstVerifiee("RAN_C_927", resultatExecution));
	}

	/**
	 * Test la règle : La date de fin prévisionnelle ou la date de fin réelle du contrat doit être obligatoirement saisie.
	 *
	 * @throws Exception
	 */
	@Test
	public void testRegleRAN_C_400() throws Exception {
		// On fait les IFait nécessaires
		ContratFait contrat = new ContratFait();
		contrat.setDateDebutLienJuridique(parseDate("01/09/2017"));
		contrat.setDateFinPrevisionelleLienJuridique(parseDate("31/08/2018"));
		contrat.setTypeContrat("CDD");
		contrat.setIdContrat("DUPOND20180016");
		contrat.setTypeLienJuridique("01");

		Set < IFait > faits = new HashSet <>();
		faits.add(contrat);

		GPResultat resultatExecution = moteurRegle.executerReglesGP(faits, Constantes.EVENEMENT_CREATION_CONTRAT);
		Assert.assertTrue("La règle RAN_C_400 doit être vérifiée", regleEstVerifiee("RAN_C_400", resultatExecution));

		ContratFait autreContrat = new ContratFait();
		autreContrat.setDateDebutLienJuridique(parseDate("01/09/2017"));

		autreContrat.setTypeContrat("CDD");
		autreContrat.setIdContrat("DUPOND20180016");
		autreContrat.setTypeLienJuridique("01");
		faits = new HashSet <>();
		faits.add(autreContrat);

		resultatExecution = moteurRegle.executerReglesGP(faits, Constantes.EVENEMENT_CREATION_CONTRAT);
		Assert.assertFalse("La règle RAN_C_400 doit être non vérifiée", regleEstVerifiee("RAN_C_400", resultatExecution));
	}

	/**
	 * Test la règle : Le mode de gestion doit être saisi au niveau du contrat.
	 *
	 * @throws Exception
	 */
	@Test
	public void testRegleRAN_C_451() throws Exception {
		// On fait les IFait nécessaires
		ContratFait contrat = new ContratFait();
		contrat.setDateDebutLienJuridique(parseDate("01/09/2017"));
		contrat.setDateFinPrevisionelleLienJuridique(parseDate("31/08/2018"));
		contrat.setTypeContrat("CDD");
		contrat.setIdContrat("DUPOND20180016");
		contrat.setTypeLienJuridique("01");

		Set < IFait > faits = new HashSet <>();
		faits.add(contrat);

		GPResultat resultatExecution = moteurRegle.executerReglesGP(faits, Constantes.EVENEMENT_CREATION_CONTRAT);
		Assert.assertTrue("La règle RAN_C_451 doit être vérifiée", regleEstVerifiee("RAN_C_451", resultatExecution));

		ContratFait autreContrat = new ContratFait();
		autreContrat.setDateDebutLienJuridique(parseDate("01/09/2017"));
		autreContrat.setDateFinPrevisionelleLienJuridique(parseDate("31/08/2018"));
		autreContrat.setTypeContrat("AE");
		autreContrat.setIdContrat("DUPOND20180016");
		autreContrat.setTypeLienJuridique("01");
		faits = new HashSet <>();
		faits.add(autreContrat);

		resultatExecution = moteurRegle.executerReglesGP(faits, Constantes.EVENEMENT_CREATION_CONTRAT);
		Assert.assertFalse("La règle RAN_C_451 doit être non vérifiée", regleEstVerifiee("RAN_C_451", resultatExecution));
	}

	/**
	 * Test la rèle : Le type de lien juridique est un contrat de droit public.
	 *
	 * @throws Exception
	 */
	@Test
	public void testRegleRAN_C_514() throws Exception {
		// On fait les IFait nécessaires
		ContratFait contrat = new ContratFait();
		contrat.setDateDebutLienJuridique(parseDate("01/09/2017"));
		contrat.setDateFinPrevisionelleLienJuridique(parseDate("31/08/2018"));
		contrat.setTypeContrat("CDD");
		contrat.setIdContrat("DUPOND20180016");
		contrat.setTypeLienJuridique("01");

		// On est obligé d'ajouter un fait pour indiquer la population
		FournisseurPopulationBasique population = new FournisseurPopulationBasique(IPopulations.MAITRE_DU_PRIVE);

		Set < IFait > faits = new HashSet <>();
		faits.add(contrat);
		faits.add(population);

		GPResultat resultatExecution = moteurRegle.executerReglesGP(faits, Constantes.EVENEMENT_CREATION_CONTRAT);
		Assert.assertTrue("La règle RAN_C_514 doit être vérifiée", regleEstVerifiee("RAN_C_514", resultatExecution));
		System.out.println(resultatExecution);

		resultatExecution = moteurRegle.executerReglesGP(faits, Constantes.EVENEMENT_CREATION_CONTRAT);
		Assert.assertTrue("La règle RAN_C_514 doit être vérifiée", regleEstVerifiee("RAN_C_514", resultatExecution));

		ContratFait autreContrat = new ContratFait();
		autreContrat.setDateDebutLienJuridique(parseDate("01/09/2017"));
		autreContrat.setDateFinPrevisionelleLienJuridique(parseDate("31/08/2018"));
		autreContrat.setTypeContrat("CDD");
		autreContrat.setIdContrat("DUPOND20180016");
		// autreContrat.setTypeLienJuridique("02");
		faits = new HashSet <>();
		faits.add(autreContrat);
		faits.add(population);

		resultatExecution = moteurRegle.executerReglesGP(faits, Constantes.EVENEMENT_CREATION_CONTRAT);
		Assert.assertTrue("La règle RAN_C_514 doit être non vérifiée", regleEstNonVerifiee("RAN_C_514", resultatExecution));
	}

	/**
	 *
	 * @param DateStr
	 *            date au format jj/mm/aaaa
	 * @return Date
	 * @throws ParseException
	 */
	private Calendar parseDate(final String DateStr) throws ParseException {

		Date date = new SimpleDateFormat("dd/MM/yyyy").parse(DateStr);
		Calendar calendrier = Calendar.getInstance();
		calendrier.setTime(date);
		return calendrier;
	}
}
