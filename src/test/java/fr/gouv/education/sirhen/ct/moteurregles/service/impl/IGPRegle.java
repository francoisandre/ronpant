package fr.gouv.education.sirhen.ct.moteurregles.service.impl;

import org.springframework.core.io.Resource;

import fr.gouv.education.sirhen.ct.moteurregles.commun.IRegle;

public interface IGPRegle extends IRegle {

	Resource getResource();

}
