package com.ehealth.mc.service.impl;

import java.util.List;

import org.apache.olingo.commons.api.data.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehealth.mc.bo.Patient;
import com.ehealth.mc.dao.PatientDAO;
import com.ehealth.mc.service.PatientService;
import com.ehealth.mc.service.util.EntityUtil;

@Service("patientService")
public class PatientServiceImpl implements PatientService {

	@Autowired
	PatientDAO patientDAO;

	@Override
	public Entity findById(Integer id) {
		List<Patient> patientList = patientDAO.findById(id);
		if (patientList != null && patientList.size() > 0) {
			Patient result = patientList.get(0);
			return EntityUtil.getEntity(result);
		}
		return null;
	}

}
