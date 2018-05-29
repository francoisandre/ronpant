package fr.gouv.education.sirhen.gin.gestiondescontratsdesnontitulaires.fait;

import java.util.Set;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.gouv.education.sirhen.ct.moteurregles.commun.IFait;
import fr.gouv.education.sirhen.ct.moteurregles.commun.impl.Resultat;
import fr.gouv.education.sirhen.ct.moteurregles.service.IMoteurReglesService;
import fr.gouv.education.sirhen.gin.gestiondescontratsdesnontitulaires.evenement.Constantes;

@ContextConfiguration(classes = TestConfiguration.class)
@RunWith(value = SpringJUnit4ClassRunner.class)
public class TestCreationContrat extends AbstractTestRegles {

	@Autowired
	IMoteurReglesService moteurRegle;

	@Test
	public void testRegleRAN_C_OO1() throws Exception {

		// On fait les IFait nécessaires
		AgentFait agent = new AgentFait();
		// ...je valorise mon agent
		ContratFait contrat = new ContratFait();
		// ...je valorise mon contrat

		Set < IFait > faits = new TreeSet <>();
		faits.add(agent);
		faits.add(contrat);

		Resultat resultatExecution = moteurRegle.executerRegles(faits, Constantes.EVENEMENT_CREATION_CONTRAT);
		Assert.assertTrue("La règle RAN_C_001 doit être vérifiée", regleEstVerifiee("RAN_C_001", resultatExecution));

		AgentFait autreAgent = new AgentFait();
		faits = new TreeSet <>();
		faits.add(autreAgent);
		faits.add(contrat);

		resultatExecution = moteurRegle.executerRegles(faits, Constantes.EVENEMENT_CREATION_CONTRAT);
		Assert.assertFalse("La règle RAN_C_001 doit être non vérifiée", regleEstVerifiee("RAN_C_001", resultatExecution));

	}

}
