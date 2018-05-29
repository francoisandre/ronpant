package fr.gouv.education.sirhen.gin.gestiondescontratsdesnontitulaires.fait;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.gouv.education.sirhen.ct.moteurregles.service.IMoteurReglesService;

@ContextConfiguration(classes = TestConfiguration.class)
@RunWith(value = SpringJUnit4ClassRunner.class)
public class DroolsTest {

	@Autowired
	IMoteurReglesService moteurRegle;

	@Test
	public void testBasique() {
		System.out.println("Nothing more...");
	}

}