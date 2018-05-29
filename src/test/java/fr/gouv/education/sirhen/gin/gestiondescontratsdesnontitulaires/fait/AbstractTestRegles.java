package fr.gouv.education.sirhen.gin.gestiondescontratsdesnontitulaires.fait;

import java.util.List;

import fr.gouv.education.sirhen.ct.moteurregles.commun.IRegle;
import fr.gouv.education.sirhen.ct.moteurregles.commun.impl.Resultat;

public abstract class AbstractTestRegles {

	protected boolean regleEstVerifiee(final String nomRegle, final Resultat resultatExecution) throws Exception {

		List < IRegle > regles = resultatExecution.getRegles();
		for (IRegle regle : regles) {
			if (regle.getCode().equalsIgnoreCase(nomRegle)) {
				return regle.getVerifiee();
			}
		}

		throw new Exception("Règle " + nomRegle + "non présente");
	}

}
