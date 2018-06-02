package fr.gouv.education.sirhen.gin.gestiondescontratsdesnontitulaires.fait;

public class FournisseurPopulationBasique implements IFournisseurPopulation {

	private String codePopulation;

	public FournisseurPopulationBasique(final String codePopulation) {
		this.codePopulation = codePopulation;
	}

	@Override
	public String getCodePopulation() {
		return codePopulation;
	}

}
