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
import com.ehealth.mc.dao.OrderDetailDAO;
import com.ehealth.mc.dao.OrderHeaderDAO;
import com.ehealth.mc.service.DoctorService;
import com.ehealth.mc.service.OrderService;
import com.ehealth.mc.service.PatientService;
import com.ehealth.mc.service.util.EntityConvertUtil;

@Service("orderService")
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

	@Autowired
	OrderHeaderDAO orderHeaderDAO;

	@Autowired
	OrderDetailDAO orderDetailDAO;

	@Autowired
	private DoctorService doctorService;

	@Autowired
	private PatientService patientService;

	@Override
	public List<OrderHeader> findAll() {

		List<OrderHeader> eList = new ArrayList<OrderHeader>();
		if (orderHeaderDAO != null) {

			Iterable<OrderHeader> result = orderHeaderDAO.findAll();
			if (result != null) {
				Iterator<OrderHeader> i = result.iterator();
				while (i.hasNext()) {
					eList.add(i.next());
				}
				return eList;
			}
		}
		return null;
	}

	@Override
	public List<OrderHeader> findByPatientID(Long patientID) {

		List<OrderHeader> eList = new ArrayList<OrderHeader>();
		if (orderHeaderDAO != null) {

			Iterable<OrderHeader> result = orderHeaderDAO
					.findByPatientID(patientID);
			if (result != null) {
				Iterator<OrderHeader> i = result.iterator();
				while (i.hasNext()) {
					eList.add(i.next());
				}
				return eList;
			}
		}
		return null;
	}

	@Override
	public List<OrderHeader> findByDoctorID(Long doctorID) {

		List<OrderHeader> eList = new ArrayList<OrderHeader>();
		if (orderHeaderDAO != null) {

			Iterable<OrderHeader> result = orderHeaderDAO
					.findByDoctorID(doctorID);
			if (result != null) {
				Iterator<OrderHeader> i = result.iterator();
				while (i.hasNext()) {
					eList.add(i.next());
				}
				return eList;
			}
		}
		return null;
	}

	@Override
	public OrderHeader findById(Long id) {
		List<OrderHeader> resultList = orderHeaderDAO.findById(id);
		if (resultList != null && resultList.size() == 1) {
			return resultList.get(0);
		}
		return null;
	}

	@Override
	@Transactional
	public OrderHeader save(Entity e) {
		Patient patient = null;
		Doctor doctor = null;
		OrderDetail orderDetail = null;

		Long patientID = EntityConvertUtil.getPatientIDFromOrderEntity(e);
		if (patientID == null) {
			return null;// Patient does not exist
		} else {
			patient = patientService.findById(patientID);
		}

		Long doctorID = EntityConvertUtil.getDoctorIDFromOrderEntity(e);

		if (doctorID != null) {
			doctor = doctorService.findById(doctorID);
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

		return orderHeader;
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
