package fr.gouv.education.sirhen.ct.moteurregles.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GPResultat {

	Map < String, ExecutionRegle > executions = new HashMap <>();

	public void ajouteExecution(final ExecutionRegle execution) {
		executions.put(execution.getRegle().getCode(), execution);
	}

	public ExecutionRegle getExecution(final String code) {
		return executions.get(code);

	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("------------");
		sb.append('\n');
		Collection < ExecutionRegle > values = executions.values();
		for (ExecutionRegle executionRegle : values) {
			sb.append(executionRegle.toString());
			sb.append('\n');
		}
		sb.append("------------");
		return sb.toString();
	}

}
