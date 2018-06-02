package fr.gouv.education.sirhen.ct.moteurregles.service.impl;

import fr.gouv.education.sirhen.ct.moteurregles.commun.IRegle;

public class ExecutionRegle {

	public final static String REGLE_IGNOREE = "REGLE_IGNOREE";
	public final static String REGLE_VERIFIEE = "REGLE_VERIFIEE";
	public final static String REGLE_NON_VERIFIEE = "REGLE_NON_VERIFIEE";

	private String typeExecution;
	private IRegle regle;
	private String commentaire;

	public ExecutionRegle(final String typeExecution) {
		this.typeExecution = typeExecution;
	}

	public ExecutionRegle(final String typeExecution, final IRegle regle) {
		this(typeExecution);
		this.regle = regle;
	}

	public ExecutionRegle(final String typeExecution, final IRegle regle, final String commentaire) {
		this(typeExecution, regle);
		this.commentaire = commentaire;
	}

	public IRegle getRegle() {
		return regle;
	}

	public void setRegle(final IRegle regle) {
		this.regle = regle;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(final String commentaire) {
		this.commentaire = commentaire;
	}

	public String getTypeExecution() {
		return typeExecution;
	}

	@Override
	public String toString() {
		if (typeExecution == REGLE_VERIFIEE) {
			return "[Règle: " + regle.getCode() + " - Execution: " + typeExecution + " ]";
		} else {
			return "[Règle: " + regle.getCode() + " - Execution: " + typeExecution + " - Commentaire: " + commentaire + " ]";
		}
	}

}
