package com.ehealth.mc.service;

import org.apache.olingo.commons.api.data.Entity;

public interface PatientService {

	Entity findById(Integer id);

	public Entity save(Entity e);

}
