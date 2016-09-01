package com.ehealth.mc.service;

import java.util.List;

import org.apache.olingo.commons.api.data.Entity;

import com.ehealth.mc.bo.Patient;

public interface PatientService {

	List<Patient> findAll();

	Patient findById(Long id);

	Patient upsertBasicInfo(Entity e);

	boolean updateIsDeleted(String value, Long[] objIDs)
			throws RuntimeException;

	Patient updateAvatar(String avatar, Long id);

	Patient updatePassword(String newPassword, Long id, String oldPassword);

	Patient findOneByLoginAndPassword(String login, String password);

	Patient findOneByLogin(String login);

	List<Patient> findByFilterString(String filterString);

	List<Patient> findByIsDeleted(String isDeleted);

}
