package com.ehealth.mc.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ehealth.mc.bo.Patient;

@Repository
public interface PatientDAO extends CrudRepository<Patient, Integer> {

	List<Patient> findById(Long id);

	@Modifying
	@Query("update Patient p set p.avatar = ?1 where p.id = ?2")
	int setAvatar(String avatar, Long id);

}
