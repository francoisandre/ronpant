package fr.gouv.education.sirhen.ct.moteurregles.service.impl;

import java.util.Map;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.Match;

import fr.gouv.education.sirhen.ct.moteurregles.commun.IRegle;
import fr.gouv.education.sirhen.ct.moteurregles.commun.impl.Regle;

public class GPAgendaFilter implements AgendaFilter, IGPAnnotations {

	private String evenement;
	private String population;
	private GPResultat resultat;

	public GPAgendaFilter(final String evenement, final String population, final GPResultat resultat) {
		this.resultat = resultat;
		this.population = population.toLowerCase();
		this.evenement = evenement.toLowerCase();
	}

	@Override
	public boolean accept(final Match match) {
		Map < String, Object > metaData = match.getRule().getMetaData();
		Map < String, String > aux = new CaseInsensitiveMap();
		for (String annotation : metaData.keySet()) {
			if (metaData.get(annotation) != null) {
				aux.put(annotation, metaData.get(annotation).toString());
			} else {
				aux.put(annotation, "");
			}
		}
		System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFF ------- " + aux.get(NOM_ANNOTATION_CODE_ONP));
		if (aux.containsKey(NOM_ANNOTATION_IGNORE)) {
			ajouteRegleIgnoree(resultat, aux.get(NOM_ANNOTATION_CODE_ONP), "Règle marquée comme devant être ignorée");
			return false;
		}

		if (!aux.containsKey(NOM_ANNOTATION_EVENEMENTS)) {
			ajouteRegleIgnoree(resultat, aux.get(NOM_ANNOTATION_CODE_ONP), "Règle ne possedant pas d'annotation evenements");
			return false;
		} else {
			String evenements = aux.get(NOM_ANNOTATION_EVENEMENTS).toLowerCase();

			if (!evenements.contains(evenement)) {
				ajouteRegleIgnoree(resultat, aux.get(NOM_ANNOTATION_CODE_ONP),
					"Règle non concernée par l'évènement " + evenement);
				return false;
			}
		}

		if (!aux.containsKey(NOM_ANNOTATION_POPULATIONS)) {
			ajouteRegleIgnoree(resultat, aux.get(NOM_ANNOTATION_CODE_ONP), "Règle ne possedant pas d'annotation populations");
			return false;
		} else {
			String populations = aux.get(NOM_ANNOTATION_POPULATIONS).toLowerCase();
			if (!populations.contains(population)) {
				ajouteRegleIgnoree(resultat, aux.get(NOM_ANNOTATION_CODE_ONP),
					"Règle non concernée par la population " + population);
				return false;
			}
		}
		// Par défaut une régle est marquée comme non vérifiée
		IRegle regle = new Regle();
		regle.setCode(aux.get(NOM_ANNOTATION_CODE_ONP));
		ExecutionRegle execution = new ExecutionRegle(ExecutionRegle.REGLE_NON_VERIFIEE, regle, aux.get(NOM_ANNOTATION_LIBELLE));
		resultat.ajouteExecution(execution);
		return true;
	}

	private void ajouteRegleIgnoree(final GPResultat resultat, final String code, final String message) {
		IRegle regle = new Regle();
		regle.setCode(code);
		ExecutionRegle execution = new ExecutionRegle(ExecutionRegle.REGLE_IGNOREE, regle, message);
		resultat.ajouteExecution(execution);
	}

}
