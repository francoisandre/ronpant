package fr.gouv.education.sirhen.ct.moteurregles.service.impl;

import org.springframework.core.io.Resource;

import fr.gouv.education.sirhen.ct.moteurregles.commun.IRegle;

public class RegleFichier extends AbstractGPRegle implements IGPRegle {

	private Resource fichierDrl;

	public RegleFichier(final IRegle regle, final Resource fichierDrl) {
		super(regle);
		this.fichierDrl = fichierDrl;
	}

	@Override
	public Resource getResource() {
		return fichierDrl;
	}

}
