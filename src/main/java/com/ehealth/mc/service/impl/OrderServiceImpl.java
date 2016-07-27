package com.ehealth.mc.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.olingo.commons.api.data.ComplexValue;
import org.apache.olingo.commons.api.data.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehealth.mc.bo.Doctor;
import com.ehealth.mc.bo.OrderDetail;
import com.ehealth.mc.bo.OrderHeader;
import com.ehealth.mc.bo.Patient;
import com.ehealth.mc.dao.DoctorDAO;
import com.ehealth.mc.dao.OrderDetailDAO;
import com.ehealth.mc.dao.OrderHeaderDAO;
import com.ehealth.mc.dao.PatientDAO;
import com.ehealth.mc.service.OrderService;
import com.ehealth.mc.service.util.EntityConvertUtil;

@Service("orderService")
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	OrderHeaderDAO orderHeaderDAO;

	@Autowired
	OrderDetailDAO orderDetailDAO;

	@Autowired
	DoctorDAO doctorDAO;

	@Autowired
	PatientDAO patientDAO;

	@Override
	public List<Entity> findAll() {

		List<Entity> eList = new ArrayList<Entity>();
		if (orderHeaderDAO != null) {

			Iterable<OrderHeader> result = orderHeaderDAO.findAll();
			if (result != null) {
				Iterator<OrderHeader> i = result.iterator();
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
		List<OrderHeader> ohList = orderHeaderDAO.findById(id);
		if (ohList != null && ohList.size() > 0) {
			OrderHeader result = ohList.get(0);
			return EntityConvertUtil.getEntity(result);
		}
		return null;
	}

	@Override
	public Entity save(Entity e) {
		Patient patient = null;
		Doctor doctor = null;
		OrderDetail orderDetail = null;

		Integer patientID = EntityConvertUtil.getPatientIDFromOrderEntity(e);
		if (patientID == null) {
			return null;// Patient does not exist
		} else {
			List<Patient> patientResultList = patientDAO.findById(patientID);
			if (patientResultList == null) {
				return null;// Patient does not exist
			} else if (patientResultList.size() == 0) {
				return null;// Patient does not exist
			} else if (patientResultList.size() > 2) {
				return null;// Duplicated patients
			}
			patient = patientResultList.get(0);
		}

		Integer doctorID = EntityConvertUtil.getDoctorIDFromOrderEntity(e);

		if (doctorID != null) {
			List<Doctor> doctorResultList = doctorDAO.findById(doctorID);
			if (doctorResultList == null) {
				return null;// Doctor does not exist
			} else if (doctorResultList.size() == 0) {
				return null;// Doctor does not exist
			} else if (doctorResultList.size() > 2) {
				return null;// Duplicated doctors
			}
			doctor = doctorResultList.get(0);
		}

		ComplexValue odToSave = EntityConvertUtil.getOrderDetailComplexValue(e);

		orderDetail = saveOrderDetail(odToSave);
		if (orderDetail == null) {
			// TODO rollback
			return null;
		}

		OrderHeader orderHeader = saveOrderHeader(e, patient, doctor,
				orderDetail);

		if (orderHeader == null) {
			// TODO rollback
			return null;
		}

		return EntityConvertUtil.getEntity(orderHeader);
	}

	private OrderHeader saveOrderHeader(Entity e, Patient patient,
			Doctor doctor, OrderDetail orderDetail) {
		OrderHeader orderHeader = EntityConvertUtil.getOrderHeader(e);
		if (orderHeader != null) {
			orderHeader.setDoctor(doctor);
			orderHeader.setPatient(patient);
			orderHeader.setOrderDetail(orderDetail);
			OrderHeader result = orderHeaderDAO.save(orderHeader);
			return result;
		}
		return null;
	}

	private OrderDetail saveOrderDetail(ComplexValue e) {
		OrderDetail objToSave = EntityConvertUtil.getOrderDetail(e);
		if (objToSave != null) {
			OrderDetail result = orderDetailDAO.save(objToSave);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

}
