package com.ehealth.mc.service;

import org.apache.olingo.commons.api.data.Entity;

import com.ehealth.mc.bo.Patient;

public interface PatientService {

	Patient findById(Long id);

	Patient save(Entity e);

}
