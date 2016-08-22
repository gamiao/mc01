package com.ehealth.mc.service.impl;

import java.util.ArrayList;
import java.util.Date;
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
		return orderHeaderDAO.findOne(id);
	}

	@Override
	@Transactional
	public OrderHeader create(Entity e) {
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

		orderDetail = createOrderDetail(odToSave);
		if (orderDetail == null) {
			// TODO rollback
			return null;
		}

		OrderHeader orderHeader = createOrderHeader(e, patient, doctor,
				orderDetail);

		if (orderHeader == null) {
			// TODO rollback
			return null;
		}

		return orderHeader;
	}

	@Override
	@Transactional
	public OrderHeader update(Entity e) {
		Patient patient = null;
		Doctor doctor = null;
		OrderDetail orderDetail = null;
		OrderHeader orderHeader = null;

		Long patientID = EntityConvertUtil.getPatientIDFromOrderEntity(e);
		if (patientID == null) {
			return null;// Patient does not exist
		} else {
			patient = patientService.findById(patientID);

			if (patient == null) {
				return null;
			}
		}

		Long orderDetailID = EntityConvertUtil
				.getOrderDetailIDFromOrderEntity(e);
		if (orderDetailID == null) {
			return null;// OrderDetail does not exist
		} else {
			orderDetail = orderDetailDAO.findOne(orderDetailID);

			if (orderDetail == null) {
				return null;
			}
		}

		Long orderHeaderID = EntityConvertUtil.getID(e);
		if (orderHeaderID == null) {
			return null;// OrderDetail does not exist
		} else {
			orderHeader = orderHeaderDAO.findOne(orderHeaderID);

			if (orderHeader == null) {
				return null;
			}
		}

		Long doctorID = EntityConvertUtil.getDoctorIDFromOrderEntity(e);

		if (doctorID != null) {
			doctor = doctorService.findById(doctorID);
			if (doctor == null) {
				return null;
			}
		}

		return updateOrderHeader(e, patient, doctor, orderDetail, orderHeader);
	}

	private OrderHeader updateOrderHeader(Entity e, Patient patient,
			Doctor doctor, OrderDetail orderDetail, OrderHeader orderHeader) {
		EntityConvertUtil.updateOrderHeader(orderHeader, e);
		orderHeader.setDoctor(doctor);
		orderHeaderDAO.save(orderHeader);
		return orderHeader;
	}

	private OrderHeader createOrderHeader(Entity e, Patient patient,
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

	private OrderDetail createOrderDetail(ComplexValue e) {
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
	@Transactional
	public OrderConversation createImageOrderConversaction(String fileName,
			Long orderHeaderID, String owner) {

		OrderHeader orderHeader = findById(orderHeaderID);

		if (orderHeader != null) {
			OrderConversation oc = new OrderConversation();
			oc.setCreateTime(new Date());
			oc.setType("IMAGE");
			oc.setOwner(owner);
			oc.setOrderHeader(orderHeader);
			oc.setPictures(fileName);
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
				.findByNoDoctorAndStatus("new");
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
