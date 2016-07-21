package com.ehealth.mc.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.olingo.commons.api.data.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehealth.mc.bo.Doctor;
import com.ehealth.mc.dao.DoctorDAO;
import com.ehealth.mc.service.DoctorService;
import com.ehealth.mc.service.util.EntityUtil;

@Service("doctorService")
public class DoctorServiceImpl implements DoctorService {

	@Autowired
	DoctorDAO dcotorDAO;

	@Override
	public List<Entity> findAll() {
		List<Entity> eList = new ArrayList<Entity>();
		if (dcotorDAO != null) {

			Iterable<Doctor> result = dcotorDAO.findAll();
			if (result != null) {
				Iterator<Doctor> i = result.iterator();
				while (i.hasNext()) {
					Entity e = EntityUtil.getEntity(i.next());
					eList.add(e);
				}
				return eList;
			}
		}
		return null;
	}

}
