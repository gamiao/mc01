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
@Transactional
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
					Entity e = EntityConvertUtil.getEntity(i.next());
					eList.add(e);
				}
				return eList;
			}
		}
		return null;
	}

	@Override
	public Entity findById(Integer id) {
		List<Doctor> list = dcotorDAO.findById(id);
		if (list != null && list.size() > 0) {
			Doctor result = list.get(0);
			return EntityConvertUtil.getEntity(result);
		}
		return null;
	}

	@Override
	public Entity save(Entity e) {
		Doctor objToSave = EntityConvertUtil.getDoctor(e);
		if (objToSave != null) {
			Doctor result = dcotorDAO.save(objToSave);
			if (result != null) {
				Entity resultEntity = EntityConvertUtil.getEntity(result);
				return resultEntity;
			}
		}
		return null;
	}

}
