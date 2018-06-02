package fr.gouv.education.sirhen.gin.gestiondescontratsdesnontitulaires.fait;

import fr.gouv.education.sirhen.ct.moteurregles.commun.IFait;

public interface IFournisseurPopulation extends IFait {

	String NO_POPULATION = "NO_POPULATION";

	public String getCodePopulation();

}
