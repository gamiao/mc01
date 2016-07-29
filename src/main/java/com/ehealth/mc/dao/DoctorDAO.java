package com.ehealth.mc.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ehealth.mc.bo.Doctor;

@Repository
public interface DoctorDAO extends CrudRepository<Doctor, Integer> {

	List<Doctor> findById(Long id);

}