package fr.gouv.education.sirhen.gin.gestiondescontratsdesnontitulaires.fait;

import java.util.Date;

import fr.gouv.education.sirhen.ct.moteurregles.commun.IFait;

public class AgentFait implements IFait {

	public enum CODE_POPULATION {
		P0199, // Maître du privé (inclus Maître du privé sur coucours et Maître délégué établ. privé sous contrat d'association)
		P0223 // Maître délégué établ. privé sous contrat simple
	}

	private Date dateEntreeFonctionPublique;
	private Date dateLimiteRetraite;
	private String codePopulation;

	public Date getDateEntreeFonctionPublique() {
		return dateEntreeFonctionPublique;
	}

	public void setDateEntreeFonctionPublique(final Date dateEntreeFonctionPublique) {
		this.dateEntreeFonctionPublique = dateEntreeFonctionPublique;
	}

	public String getCodePopulation() {
		return codePopulation;
	}

	public void setCodePopulation(final String codePopulation) {
		this.codePopulation = codePopulation;
	}

	public Date getDateLimiteRetraite() {
		return dateLimiteRetraite;
	}

	public void setDateLimiteRetraite(final Date dateLimiteRetraite) {
		this.dateLimiteRetraite = dateLimiteRetraite;
	}

}
