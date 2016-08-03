package com.ehealth.mc.service;

import java.util.List;

import org.apache.olingo.commons.api.data.Entity;

import com.ehealth.mc.bo.Doctor;
import com.ehealth.mc.bo.Patient;

public interface DoctorService {

	List<Doctor> findAll();

	Doctor findById(Long id);

	Doctor save(Entity e);

	Doctor updateAvatar(String avatar, Long id);

}
