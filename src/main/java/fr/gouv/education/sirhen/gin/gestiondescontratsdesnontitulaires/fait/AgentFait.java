package fr.gouv.education.sirhen.gin.gestiondescontratsdesnontitulaires.fait;

import java.util.Calendar;

import fr.gouv.education.sirhen.ct.moteurregles.commun.IFait;

public class AgentFait implements IFait, IFournisseurPopulation {

	private Calendar dateEntreeFonctionPublique;
	private Calendar dateLimiteRetraite;
	private String codePopulation;

	public Calendar getDateEntreeFonctionPublique() {
		return dateEntreeFonctionPublique;
	}

	public void setDateEntreeFonctionPublique(final Calendar dateEntreeFonctionPublique) {
		this.dateEntreeFonctionPublique = dateEntreeFonctionPublique;
	}

	public String getCodePopulation() {
		return codePopulation;
	}

	public void setCodePopulation(final String codePopulation) {
		this.codePopulation = codePopulation;
	}

	public Calendar getDateLimiteRetraite() {
		return dateLimiteRetraite;
	}

	public void setDateLimiteRetraite(final Calendar dateLimiteRetraite) {
		this.dateLimiteRetraite = dateLimiteRetraite;
	}

}
