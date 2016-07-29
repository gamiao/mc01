package com.ehealth.mc.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ehealth.mc.bo.Patient;

@Repository
public interface PatientDAO extends CrudRepository<Patient, Integer> {

	List<Patient> findById(Long id);

}
