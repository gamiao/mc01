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
	public Entity findById(Integer id) {
		List<Patient> patientList = patientDAO.findById(id);
		if (patientList != null && patientList.size() > 0) {
			Patient result = patientList.get(0);
			return EntityConvertUtil.getEntity(result);
		}
		return null;
	}

	@Override
	public Entity save(Entity e) {
		Patient objToSave = EntityConvertUtil.getPatient(e);
		if (objToSave != null) {
			Patient result = patientDAO.save(objToSave);
			if (result != null) {
				Entity resultEntity = EntityConvertUtil.getEntity(result);
				return resultEntity;
			}
		}
		return null;
	}

}
