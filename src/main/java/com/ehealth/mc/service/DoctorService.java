package com.ehealth.mc.service;

import java.util.List;

import org.apache.olingo.commons.api.data.Entity;

import com.ehealth.mc.bo.Doctor;

public interface DoctorService {

	List<Doctor> findAll();

	Doctor findById(Long id);

	Doctor upsertBasicInfo(Entity e);

	boolean updateIsDeleted(String value, Long[] objIDs)
			throws RuntimeException;

	Doctor updateAvatar(String avatar, Long id);

	Doctor updatePassword(String newPassword, Long id, String oldPassword);

	List<Doctor> findByIsDeleted(String isDeleted);

	Doctor findOneByLoginAndPassword(String login, String password);

	Doctor findOneByLogin(String login);

	List<Doctor> findByFilterString(String filterString);

	Doctor findOneByMail(String mail);

	void mailAccountInfo(Doctor user);

	boolean mailAccountInfoByMail(String mail);

}
