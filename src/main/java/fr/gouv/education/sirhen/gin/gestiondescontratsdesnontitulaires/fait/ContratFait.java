package fr.gouv.education.sirhen.gin.gestiondescontratsdesnontitulaires.fait;

import java.util.Date;

import fr.gouv.education.sirhen.ct.moteurregles.commun.IFait;

public class ContratFait implements IFait {

	private Date dateDebutLienJuridique;
	private Date dateFinPrevisionelleLienJuridique;
	private Date dateFinReelLienJuridique;
	private String typeContrat;
	private String idContrat;
	private String typeLienJuridique;

	public Date getDateDebutLienJuridique() {
		return dateDebutLienJuridique;
	}

	public void setDateDebutLienJuridique(final Date dateDebutLienJuridique) {
		this.dateDebutLienJuridique = dateDebutLienJuridique;
	}

	public Date getDateFinPrevisionelleLienJuridique() {
		return dateFinPrevisionelleLienJuridique;
	}

	public void setDateFinPrevisionelleLienJuridique(final Date dateFinPrevisionelleLienJuridique) {
		this.dateFinPrevisionelleLienJuridique = dateFinPrevisionelleLienJuridique;
	}

	public Date getDateFinReelLienJuridique() {
		return dateFinReelLienJuridique;
	}

	public void setDateFinReelLienJuridique(final Date dateFinReelLienJuridique) {
		this.dateFinReelLienJuridique = dateFinReelLienJuridique;
	}

	public String getTypeContrat() {
		return typeContrat;
	}

	public void setTypeContrat(final String typeContrat) {
		this.typeContrat = typeContrat;
	}

	public String getIdContrat() {
		return idContrat;
	}

	public void setIdContrat(final String idContrat) {
		this.idContrat = idContrat;
	}

	/**
	 * Accesseur en lecture de l'attribut <code>typeLienJuridique</code>.
	 *
	 * @return typeLienJuridique
	 */
	public String getTypeLienJuridique() {
		return typeLienJuridique;
	}

	/**
	 * Accesseur en écriture de l'attribut <code>typeLienJuridique</code>.
	 *
	 * @param typeLienJuridique
	 *            String
	 *
	 */
	public void setTypeLienJuridique(final String typeLienJuridique) {
		this.typeLienJuridique = typeLienJuridique;
	}

}
