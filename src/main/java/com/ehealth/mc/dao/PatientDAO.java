package com.ehealth.mc.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ehealth.mc.bo.Patient;

@Repository
public interface PatientDAO extends CrudRepository<Patient, Long> {

	Patient findOne(Long id);

	@Modifying
	@Query("update Patient p set p.avatar = ?1 where p.id = ?2")
	int setAvatar(String avatar, Long id);

	@Modifying
	@Query("update Patient p set p.password = ?1 where p.id = ?2 and p.password = ?3")
	int updatePassword(String newPassword, Long id, String oldPassword);

	Patient findOneByLoginAndPassword(String login, String password);

	Patient findOneByLogin(String login);

}
