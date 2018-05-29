package fr.gouv.education.sirhen.gin.gestiondescontratsdesnontitulaires.fait;

import java.util.Date;

import fr.gouv.education.sirhen.ct.moteurregles.commun.IFait;

public class ContratFait implements IFait {

	private Date dateDebutLienJuridique;

	public Date getDateDebutLienJuridique() {
		return dateDebutLienJuridique;
	}

	public void setDateDebutLienJuridique(final Date dateDebutLienJuridique) {
		this.dateDebutLienJuridique = dateDebutLienJuridique;
	}

}
