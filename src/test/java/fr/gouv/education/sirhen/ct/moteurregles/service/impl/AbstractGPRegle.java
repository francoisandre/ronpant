package fr.gouv.education.sirhen.ct.moteurregles.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fr.gouv.education.sirhen.ct.moteurregles.commun.IRegle;

public abstract class AbstractGPRegle implements IGPRegle {

	private IRegle regle;

	public AbstractGPRegle(final IRegle regle) {
		this.regle = regle;
	}

	@Override
	public String getCode() {
		return regle.getCode();
	}

	@Override
	public Boolean getBloquante() {
		return regle.getBloquante();
	}

	@Override
	public void setBloquante(final Boolean bloquante) {
		regle.setBloquante(bloquante);

	}

	@Override
	public Calendar getDateDebutApplication() {
		return regle.getDateDebutApplication();
	}

	@Override
	public void setDateDebutApplication(final Date dateDebutApplication) {
		regle.setDateDebutApplication(dateDebutApplication);
	}

	@Override
	public void setDateFinApplication(final Date dateFinApplication) {
		regle.setDateDebutApplication(dateFinApplication);
	}

	@Override
	public Calendar getDateFinApplication() {
		return regle.getDateFinApplication();
	}

	@Override
	public void setCode(final String code) {
		regle.setCode(code);

	}

	@Override
	public void setVerifiee(final Boolean verifiee) {
		regle.setVerifiee(verifiee);
	}

	@Override
	public Boolean getVerifiee() {
		return regle.getVerifiee();
	}

	@Override
	public Boolean getValide() {
		return regle.getValide();
	}

	@Override
	public boolean isAppartientPopulation(final String population) {
		return regle.isAppartientPopulation(population);
	}

	@Override
	public void addDerogation(final String numen) {
		regle.addDerogation(numen);
	}

	@Override
	public List < String > getPopulations() {
		return regle.getPopulations();
	}

	@Override
	public List < String > getDerogations() {
		return regle.getDerogations();
	}

	@Override
	public void setDerogee(final Boolean derogee) {
		regle.setDerogee(derogee);
	}

	@Override
	public Boolean getDerogee() {
		return regle.getDerogee();
	}

	@Override
	public String getLibelle() {
		return regle.getLibelle();
	}

	@Override
	public void setLibelle(final String libelle) {
		regle.setLibelle(libelle);
	}

	@Override
	public List < String > getEvenements() {
		return regle.getEvenements();
	}

	@Override
	public boolean isAppartientEvenement(final String evenement) {
		return regle.isAppartientEvenement(evenement);
	}

}
