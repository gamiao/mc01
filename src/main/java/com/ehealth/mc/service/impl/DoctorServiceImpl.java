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
		List<Doctor> resultList = doctorDAO.findById(id);
		if (resultList != null && resultList.size() == 1) {
			return resultList.get(0);
		}
		return null;
	}

	@Override
	@Transactional
	public Doctor save(Entity e) {
		Doctor objToSave = EntityConvertUtil.getDoctor(e);
		if (objToSave != null) {
			Doctor result = doctorDAO.save(objToSave);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

}
