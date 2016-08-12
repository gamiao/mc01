package com.ehealth.mc.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ehealth.mc.bo.Doctor;

@Repository
public interface DoctorDAO extends CrudRepository<Doctor, Long> {

	Doctor findOne(Long id);

	@Modifying
	@Query("update Doctor d set d.avatar = ?1 where d.id = ?2")
	int setAvatar(String avatar, Long id);

	List<Doctor> findByIsDeleted(String isDeleted);

}