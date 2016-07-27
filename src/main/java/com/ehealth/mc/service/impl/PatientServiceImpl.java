package com.ehealth.mc.service.impl;

import java.util.List;

import org.apache.olingo.commons.api.data.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehealth.mc.bo.Patient;
import com.ehealth.mc.dao.PatientDAO;
import com.ehealth.mc.service.PatientService;
import com.ehealth.mc.service.util.EntityConvertUtil;

@Service("patientService")
@Transactional
public class PatientServiceImpl implements PatientService {

	@Autowired
	PatientDAO patientDAO;

	@Override
	public Patient findById(Integer id) {
		List<Patient> resultList = patientDAO.findById(id);
		if (resultList != null && resultList.size() == 1) {
			return resultList.get(0);
		}
		return null;
	}

	@Override
	public Patient save(Entity e, Patient originalObj) {
		Patient objToSave = EntityConvertUtil.getPatient(e, originalObj);
		if (objToSave != null) {
			Patient result = patientDAO.save(objToSave);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

}
