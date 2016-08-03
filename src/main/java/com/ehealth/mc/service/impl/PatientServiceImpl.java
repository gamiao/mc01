package com.ehealth.mc.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.olingo.commons.api.data.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehealth.mc.bo.Patient;
import com.ehealth.mc.dao.PatientDAO;
import com.ehealth.mc.service.PatientService;
import com.ehealth.mc.service.util.EntityConvertUtil;

@Service("patientService")
@Transactional(readOnly = true)
public class PatientServiceImpl implements PatientService {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	PatientDAO patientDAO;

	@Override
	public Patient findById(Long id) {
		List<Patient> resultList = patientDAO.findById(id);
		if (resultList != null && resultList.size() == 1) {
			return resultList.get(0);
		}
		return null;
	}

	@Override
	@Transactional
	public Patient save(Entity e) {
		Patient objToSave = EntityConvertUtil.getPatient(e);
		if (objToSave != null) {
			patientDAO.save(objToSave);
			return objToSave;
		}
		return null;
	}

	@Override
	@Transactional
	public Patient updateAvatar(String avatar, Long id) {
		int updatedCount = patientDAO.setAvatar(avatar, id);
		if (updatedCount > 0) {
			return findById(id);
		}
		return null;
	}

}
