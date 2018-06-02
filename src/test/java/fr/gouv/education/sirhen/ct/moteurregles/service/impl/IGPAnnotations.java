package fr.gouv.education.sirhen.ct.moteurregles.service.impl;

public interface IGPAnnotations {

	String NOM_ANNOTATION_CODE_ONP = "codeonp";
	String NOM_ANNOTATION_NOYAU_ONP = "noyauonp";
	String NOM_ANNOTATION_EVENEMENTS = "evenements";
	/**
	 * Annotation indiquant la date de début de validité d'une règle au format JJ-MM-AAAA
	 */
	String NOM_ANNOTATION_DATE_DEBUT_VALIDITE = "datedebutvalidite";
	/**
	 * Annotation indiquant la date de fin de validité d'une règle au format JJ-MM-AAAA
	 */
	String NOM_ANNOTATION_DATE_FIN_VALIDITE = "datefinvalidite";

	String NOM_ANNOTATION_BLOQUANTE = "bloquante";
	String NOM_ANNOTATION_IGNORE = "ignore";
	String NOM_ANNOTATION_LIBELLE = "libelle";
	String NOM_ANNOTATION_COMMENTAIRE = "commentaire";
	String NOM_ANNOTATION_POPULATIONS = "populations";

}
