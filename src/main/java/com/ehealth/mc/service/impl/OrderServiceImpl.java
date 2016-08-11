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
import com.ehealth.mc.bo.OrderConversation;
import com.ehealth.mc.bo.OrderDetail;
import com.ehealth.mc.bo.OrderHeader;
import com.ehealth.mc.bo.Patient;
import com.ehealth.mc.dao.OrderConversationDAO;
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
	private OrderConversationDAO orderConversationDAO;

	@Autowired
	private OrderHeaderDAO orderHeaderDAO;

	@Autowired
	private OrderDetailDAO orderDetailDAO;

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

	@Override
	public List<OrderHeader> findByPatientIDAndStatus(Long id, String status) {
		return orderHeaderDAO.findByPatientIDAndStatus(id, status);
	}

	@Override
	public List<OrderHeader> findByDoctorIDAndStatus(Long id, String status) {
		return orderHeaderDAO.findByDoctorIDAndStatus(id, status);
	}

	@Override
	public List<OrderHeader> findByPatientID(Long id) {
		return orderHeaderDAO.findByPatientID(id);
	}

	@Override
	public List<OrderHeader> findByDoctorID(Long id) {
		return orderHeaderDAO.findByDoctorID(id);
	}

	@Override
	public List<OrderHeader> findByPatientIDArchived(Long id) {
		return orderHeaderDAO.findByPatientIDArchived(id);
	}

	@Override
	public List<OrderHeader> findByDoctorIDArchived(Long id) {
		return orderHeaderDAO.findByDoctorIDArchived(id);
	}

	@Override
	public List<OrderHeader> findByPatientIDNotArchived(Long id) {
		return orderHeaderDAO.findByPatientIDNotArchived(id);
	}

	@Override
	public List<OrderHeader> findByDoctorIDNotArchived(Long id) {
		return orderHeaderDAO.findByDoctorIDNotArchived(id);
	}

	@Override
	@Transactional
	public OrderConversation createOrderConversaction(Entity newEntity,
			Entity parentEntity) {

		Long orderHeaderID = EntityConvertUtil.getID(parentEntity);
		OrderHeader orderHeader = findById(orderHeaderID);

		if (orderHeader != null) {
			OrderConversation oc = EntityConvertUtil
					.getOrderConversation(newEntity);
			oc.setOrderHeader(orderHeader);
			orderConversationDAO.save(oc);

			return oc;
		}
		return null;
	}

	@Override
	public List<OrderHeader> findByDoctorIDForPickUp(Long id) {
		List<OrderHeader> orderToBeConfirmed = orderHeaderDAO
				.findByDoctorIDAndStatus(id, "new");
		List<OrderHeader> orderToPickUp = orderHeaderDAO
				.findByDoctorIDAndStatus(null, "new");
		if (null == orderToBeConfirmed && null == orderToPickUp) {
			return null;
		}

		List<OrderHeader> resultList = new ArrayList<OrderHeader>();
		if (orderToBeConfirmed != null && orderToBeConfirmed.size() > 0) {
			resultList.addAll(orderToBeConfirmed);
		}
		if (orderToPickUp != null && orderToPickUp.size() > 0) {
			resultList.addAll(orderToPickUp);
		}
		return resultList;
	}

}
