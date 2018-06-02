package fr.gouv.education.sirhen.ct.moteurregles.service.impl;

import org.kie.api.definition.rule.Rule;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.AgendaEventListener;
import org.kie.api.event.rule.AgendaGroupPoppedEvent;
import org.kie.api.event.rule.AgendaGroupPushedEvent;
import org.kie.api.event.rule.BeforeMatchFiredEvent;
import org.kie.api.event.rule.MatchCancelledEvent;
import org.kie.api.event.rule.MatchCreatedEvent;
import org.kie.api.event.rule.RuleFlowGroupActivatedEvent;
import org.kie.api.event.rule.RuleFlowGroupDeactivatedEvent;

import fr.gouv.education.sirhen.ct.moteurregles.commun.IRegle;
import fr.gouv.education.sirhen.ct.moteurregles.commun.impl.Regle;

public class GPAgendaEventListener implements AgendaEventListener {

	private GPResultat resultat;

	public GPAgendaEventListener(final GPResultat resultat) {
		this.resultat = resultat;
	}

	@Override
	public void matchCreated(final MatchCreatedEvent event) {

	}

	@Override
	public void matchCancelled(final MatchCancelledEvent event) {

	}

	@Override
	public void beforeMatchFired(final BeforeMatchFiredEvent event) {

	}

	@Override
	public void agendaGroupPopped(final AgendaGroupPoppedEvent event) {

	}

	@Override
	public void agendaGroupPushed(final AgendaGroupPushedEvent event) {

	}

	@Override
	public void beforeRuleFlowGroupActivated(final RuleFlowGroupActivatedEvent event) {

	}

	@Override
	public void afterRuleFlowGroupActivated(final RuleFlowGroupActivatedEvent event) {

	}

	@Override
	public void beforeRuleFlowGroupDeactivated(final RuleFlowGroupDeactivatedEvent event) {

	}

	@Override
	public void afterRuleFlowGroupDeactivated(final RuleFlowGroupDeactivatedEvent event) {

	}

	@Override
	public void afterMatchFired(final AfterMatchFiredEvent event) {
		Rule rule = event.getMatch().getRule();
		IRegle regle = new Regle();
		regle.setCode(rule.getName());
		resultat.ajouteExecution(new ExecutionRegle(ExecutionRegle.REGLE_VERIFIEE, regle));
	}

}
