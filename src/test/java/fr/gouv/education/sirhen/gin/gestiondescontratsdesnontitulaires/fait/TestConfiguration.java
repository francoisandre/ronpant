package fr.gouv.education.sirhen.gin.gestiondescontratsdesnontitulaires.fait;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.gouv.education.sirhen.ct.moteurregles.service.IMoteurReglesService;
import fr.gouv.education.sirhen.ct.moteurregles.service.impl.GPMoteurReglesServiceImpl;
import fr.gouv.education.sirhen.ct.moteurregles.service.impl.MoteurReglesServiceImpl;
import fr.gouv.education.sirhen.ct.moteurregles.transverse.Constantes;
import fr.gouv.education.sirhen.ct.socle.configuration.ConfigurationComposantTechnique;

@Configuration
public class TestConfiguration {

	public ConfigurationComposantTechnique getConfiguration() {

		ConfigurationComposantTechnique resultat = new ConfigurationComposantTechnique();
		Properties properties = resultat.getProperties();
		properties.put(Constantes.CLE_KJAR_FILE, "toto");
		return resultat;

	}

	@Autowired
	private ApplicationContext appContext;

	@Bean
	public IMoteurReglesService getMoteurRegles() {
		MoteurReglesServiceImpl resultat = new GPMoteurReglesServiceImpl(appContext, getConfiguration());
		return resultat;
	}

}