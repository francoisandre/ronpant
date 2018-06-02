package fr.gouv.education.sirhen.gin.gestiondescontratsdesnontitulaires.fait;

import java.util.Date;

import fr.gouv.education.sirhen.ct.moteurregles.commun.IFait;

public class AgentFait implements IFait, IFournisseurPopulation {

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
