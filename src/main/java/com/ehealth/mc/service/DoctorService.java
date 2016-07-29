package com.ehealth.mc.service;

import java.util.List;

import org.apache.olingo.commons.api.data.Entity;

import com.ehealth.mc.bo.Doctor;

public interface DoctorService {

	List<Doctor> findAll();

	Doctor findById(Long id);

	Doctor save(Entity e);

}
