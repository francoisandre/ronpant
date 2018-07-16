package fr.gouv.education.sirhen.gin.gestiondescontratsdesnontitulaires.fait;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.gouv.education.sirhen.ct.moteurregles.service.impl.GPMoteurReglesServiceImpl;

@Configuration
public class TestConfiguration {

	@Bean
	public GPMoteurReglesServiceImpl getMoteurRegles() throws Exception {
		GPMoteurReglesServiceImpl resultat = new GPMoteurReglesServiceImpl();
		return resultat;
	}

}