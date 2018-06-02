package fr.gouv.education.sirhen.gin.gestiondescontratsdesnontitulaires.fait;

import fr.gouv.education.sirhen.ct.moteurregles.service.impl.ExecutionRegle;
import fr.gouv.education.sirhen.ct.moteurregles.service.impl.GPResultat;

public abstract class AbstractTestRegles {

	protected boolean regleEstVerifiee(final String nomRegle, final GPResultat resultatExecution) throws Exception {

		ExecutionRegle execution = resultatExecution.getExecution(nomRegle);
		if (execution == null) {
			throw new Exception("Règle " + nomRegle + "non présente dans l'exécution");
		} else {
			if (execution.getTypeExecution().equals(ExecutionRegle.REGLE_VERIFIEE)) {
				return true;
			} else {
				throw new Exception("La règle " + nomRegle + "a comme statut: " + execution.getTypeExecution());
			}
		}

	}

	protected boolean regleEstNonVerifiee(final String nomRegle, final GPResultat resultatExecution) throws Exception {

		ExecutionRegle execution = resultatExecution.getExecution(nomRegle);
		if (execution == null) {
			throw new Exception("Règle " + nomRegle + "non présente dans l'exécution");
		} else {
			if (execution.getTypeExecution().equals(ExecutionRegle.REGLE_NON_VERIFIEE)) {
				return true;
			} else {
				throw new Exception("La règle " + nomRegle + "a comme statut: " + execution.getTypeExecution());
			}
		}

	}

}
