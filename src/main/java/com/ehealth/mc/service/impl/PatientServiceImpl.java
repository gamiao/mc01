package com.ehealth.mc.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
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
import com.ehealth.mc.service.util.QueryExpressionUtil;
import com.querydsl.core.types.Predicate;

@Service("patientService")
@Transactional(readOnly = true)
public class PatientServiceImpl implements PatientService {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	PatientDAO patientDAO;

	@Override
	public Patient findById(Long id) {
		return patientDAO.findOne(id);
	}

	@Override
	@Transactional
	public Patient upsertBasicInfo(Entity e) {
		Long id = EntityConvertUtil.getID(e);
		Patient original = null;
		if (id != null) {
			original = findById(id);
		}
		Patient objToSave = EntityConvertUtil.getPatient(e, original);
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

	@Override
	@Transactional
	public Patient updatePassword(String newPassword, Long id,
			String oldPassword) {
		int updatedCount = patientDAO.updatePassword(newPassword, id,
				oldPassword);
		if (updatedCount > 0) {
			return findById(id);
		}
		return null;
	}

	@Override
	public Patient findOneByLoginAndPassword(String login, String password) {
		return patientDAO.findOneByLoginAndPassword(login, password);
	}

	@Override
	public Patient findOneByLogin(String login) {
		return patientDAO.findOneByLogin(login);
	}

	@Override
	public List<Patient> findByFilterString(String filterString) {

		Predicate querys = QueryExpressionUtil
				.getPatientWhereClausesByFilterString(filterString);
		if (querys != null) {
			Iterable<Patient> result = patientDAO.findAll(querys);
			if (result != null) {
				List<Patient> eList = new ArrayList<Patient>();
				Iterator<Patient> i = result.iterator();
				while (i.hasNext()) {
					eList.add(i.next());
				}
				return eList;
			}
		}
		return null;
	}

	@Override
	public List<Patient> findAll() {
		List<Patient> eList = new ArrayList<Patient>();
		if (patientDAO != null) {

			Iterable<Patient> result = patientDAO.findAll();
			if (result != null) {
				Iterator<Patient> i = result.iterator();
				while (i.hasNext()) {
					eList.add(i.next());
				}
				return eList;
			}
		}
		return null;
	}

}
