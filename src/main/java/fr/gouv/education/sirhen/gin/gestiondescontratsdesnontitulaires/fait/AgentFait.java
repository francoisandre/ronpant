package fr.gouv.education.sirhen.gin.gestiondescontratsdesnontitulaires.fait;

import java.util.Date;

import fr.gouv.education.sirhen.ct.moteurregles.commun.IFait;

public class AgentFait implements IFait {

	private Date dateEntreeFonctionPublique;

	public Date getDateEntreeFonctionPublique() {
		return dateEntreeFonctionPublique;
	}

	public void setDateEntreeFonctionPublique(final Date dateEntreeFonctionPublique) {
		this.dateEntreeFonctionPublique = dateEntreeFonctionPublique;
	}

}
