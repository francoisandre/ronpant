package fr.gouv.education.sirhen.ct.moteurregles.service.impl;

import java.util.ArrayList;

public class CaseInsensitiveList extends ArrayList < String > {
	@Override
	public boolean contains(final Object o) {
		String paramStr = (String) o;
		for (String s : this) {
			if (paramStr.equalsIgnoreCase(s)) {
				return true;
			}
		}
		return false;
	}

	public String getOriginalValue(final String value) {
		for (String s : this) {
			if (value.equalsIgnoreCase(s)) {
				return s;
			}
		}
		return value;
	}
}