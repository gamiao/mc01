package com.ehealth.mc.service;

import java.util.List;

import org.apache.olingo.commons.api.data.Entity;

public interface DoctorService {

	List<Entity> findAll();

	Entity findById(Integer id);

	Entity save(Entity e);

}
