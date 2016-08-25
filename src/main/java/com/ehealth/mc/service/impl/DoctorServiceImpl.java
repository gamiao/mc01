package com.ehealth.mc.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.olingo.commons.api.data.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehealth.mc.bo.Doctor;
import com.ehealth.mc.dao.DoctorDAO;
import com.ehealth.mc.service.DoctorService;
import com.ehealth.mc.service.util.EntityConvertUtil;

@Service("doctorService")
@Transactional(readOnly = true)
public class DoctorServiceImpl implements DoctorService {

	@Autowired
	DoctorDAO doctorDAO;

	@Override
	public List<Doctor> findAll() {
		List<Doctor> eList = new ArrayList<Doctor>();
		if (doctorDAO != null) {

			Iterable<Doctor> result = doctorDAO.findAll();
			if (result != null) {
				Iterator<Doctor> i = result.iterator();
				while (i.hasNext()) {
					eList.add(i.next());
				}
				return eList;
			}
		}
		return null;
	}

	@Override
	public Doctor findById(Long id) {
		return doctorDAO.findOne(id);
	}

	@Override
	@Transactional
	public Doctor upsertBasicInfo(Entity e) {
		Long id = EntityConvertUtil.getID(e);
		Doctor original = null;
		if (id != null) {
			original = findById(id);
		}
		Doctor objToSave = EntityConvertUtil.getDoctor(e, original);
		if (objToSave != null) {
			Doctor result = doctorDAO.save(objToSave);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	@Override
	@Transactional
	public Doctor updateAvatar(String avatar, Long id) {
		int updatedCount = doctorDAO.setAvatar(avatar, id);
		if (updatedCount > 0) {
			return findById(id);
		}
		return null;
	}

	@Override
	@Transactional
	public Doctor updatePassword(String newPassword, Long id, String oldPassword) {
		int updatedCount = doctorDAO.updatePassword(newPassword, id,
				oldPassword);
		if (updatedCount > 0) {
			return findById(id);
		}
		return null;
	}

	@Override
	public List<Doctor> findByIsDeleted(String isDeleted) {
		return doctorDAO.findByIsDeleted(isDeleted);
	}

	@Override
	public Doctor findOneByLoginAndPassword(String login, String password) {
		return doctorDAO.findOneByLoginAndPassword(login, password);
	}

	@Override
	public Doctor findOneByLogin(String login) {
		return doctorDAO.findOneByLogin(login);
	}

}
