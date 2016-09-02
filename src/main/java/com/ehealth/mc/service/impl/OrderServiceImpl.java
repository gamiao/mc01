package com.ehealth.mc.service.impl;

import java.util.Date;
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
import com.ehealth.mc.bo.OrderHeaderCL;
import com.ehealth.mc.bo.Patient;
import com.ehealth.mc.bo.QOrderHeader;
import com.ehealth.mc.dao.OrderConversationDAO;
import com.ehealth.mc.dao.OrderDetailDAO;
import com.ehealth.mc.dao.OrderHeaderCLDAO;
import com.ehealth.mc.dao.OrderHeaderDAO;
import com.ehealth.mc.service.DoctorService;
import com.ehealth.mc.service.OrderService;
import com.ehealth.mc.service.PatientService;
import com.ehealth.mc.service.util.EntityConvertUtil;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.querydsl.core.types.dsl.BooleanExpression;

@Service("orderService")
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

	private static Gson gson = new Gson();

	@Autowired
	private OrderConversationDAO orderConversationDAO;

	@Autowired
	private OrderHeaderDAO orderHeaderDAO;

	@Autowired
	private OrderHeaderCLDAO orderHeaderCLDAO;

	@Autowired
	private OrderDetailDAO orderDetailDAO;

	@Autowired
	private DoctorService doctorService;

	@Autowired
	private PatientService patientService;

	@Override
	public List<OrderHeader> findAll() {
		return Lists.newArrayList(orderHeaderDAO.findAll());
	}

	@Override
	public OrderHeader findById(Long id) {
		return orderHeaderDAO.findOne(id);
	}

	@Override
	@Transactional(rollbackFor = RuntimeException.class)
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
			throw new RuntimeException("Can't create orderDetail object.");
		}

		OrderHeader orderHeader = createOrderHeader(e, patient, doctor,
				orderDetail);

		if (orderHeader == null) {
			throw new RuntimeException("Can't create orderHeader object.");
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

		String beforeChange = gson.toJson(orderHeader);
		EntityConvertUtil.updateOrderHeader(orderHeader, e);
		orderHeader.setDoctor(doctor);
		// TODO

		orderHeaderDAO.save(orderHeader);
		String afterChange = gson.toJson(orderHeader);
		String operator = "S";
		String operationType = "UPDATE";
		createOrderHeaderCL(beforeChange, afterChange, orderHeader,
				operationType, operator);

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
			createOrderHeaderCL(null, gson.toJson(orderHeader), result,
					"CREATE", "P");
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

			List<OrderConversation> convs = orderHeader.getOrderConversations();
			int doctorTextConvNumber = 0;
			for (OrderConversation conv : convs) {
				if (conv != null && "D".equals(conv.getOwner())
						&& "TEXT".equals(conv.getType())) {
					doctorTextConvNumber += 1;
				}
			}

			if (doctorTextConvNumber >= 3) {
				String beforeChange = gson.toJson(orderHeader);
				orderHeader.setStatus("complete");
				String afterChange = gson.toJson(orderHeader);
				String operator = "S";
				String operationType = "UPDATE";
				orderHeaderDAO.save(orderHeader);
				createOrderHeaderCL(beforeChange, afterChange, orderHeader,
						operationType, operator);
			}

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
	@Transactional(rollbackFor = RuntimeException.class)
	public boolean updateIsDeleted(String value, Long[] objIDs)
			throws RuntimeException {
		for (Long id : objIDs) {
			if (id == null) {
				throw new RuntimeException("Some id is empty!");
			}
			OrderHeader obj = orderHeaderDAO.findOne(id);
			if (obj == null) {
				throw new RuntimeException("Can not find the object with ID:"
						+ id);
			}
			String beforeChange = gson.toJson(obj);
			obj.setIsDeleted(value);
			String afterChange = gson.toJson(obj);

			String operator = "S";
			String operationType = "UPDATE";

			try {
				orderHeaderDAO.save(obj);
				createOrderHeaderCL(beforeChange, afterChange, obj,
						operationType, operator);
			} catch (Exception e) {
				throw new RuntimeException("Can't update the object with ID:"
						+ id);
			}
		}
		return true;
	}

	@Override
	@Transactional(rollbackFor = RuntimeException.class)
	public boolean updateIsArchived(String value, Long[] objIDs)
			throws RuntimeException {
		for (Long id : objIDs) {
			if (id == null) {
				throw new RuntimeException("Some id is empty!");
			}
			OrderHeader obj = orderHeaderDAO.findOne(id);
			if (obj == null) {
				throw new RuntimeException("Can not find the object with ID:"
						+ id);
			}
			String beforeChange = gson.toJson(obj);
			obj.setIsArchived(value);
			String afterChange = gson.toJson(obj);

			String operator = "S";
			String operationType = "UPDATE";

			try {
				orderHeaderDAO.save(obj);
				createOrderHeaderCL(beforeChange, afterChange, obj,
						operationType, operator);
			} catch (Exception e) {
				throw new RuntimeException("Can't update the object with ID:"
						+ id);
			}
		}
		return true;
	}

	@Override
	public List<OrderHeader> findByPatientID(Long patientID) {
		QOrderHeader orderHeader = QOrderHeader.orderHeader;
		BooleanExpression exp1 = orderHeader.patient.id.eq(patientID);
		BooleanExpression exp2 = orderHeader.isDeleted.eq("N");
		return Lists.newArrayList(orderHeaderDAO.findAll(exp1.and(exp2)));
	}

	@Override
	public List<OrderHeader> findByDoctorID(Long doctorID) {
		QOrderHeader orderHeader = QOrderHeader.orderHeader;
		BooleanExpression exp1 = orderHeader.doctor.id.eq(doctorID);
		BooleanExpression exp2 = orderHeader.isDeleted.eq("N");
		return Lists.newArrayList(orderHeaderDAO.findAll(exp1.and(exp2)));
	}

	@Override
	public List<OrderHeader> findByDoctorIDAndIsArchived(Long doctorID,
			String isArchivedStr) {
		QOrderHeader orderHeader = QOrderHeader.orderHeader;
		BooleanExpression exp1 = orderHeader.doctor.id.eq(doctorID);
		BooleanExpression exp2 = orderHeader.isDeleted.eq("N");
		BooleanExpression exp3 = orderHeader.isArchived.eq(isArchivedStr);
		BooleanExpression exp4 = orderHeader.status.ne("new");
		return Lists.newArrayList(orderHeaderDAO.findAll(exp1.and(exp2)
				.and(exp3).and(exp4)));
	}

	@Override
	public List<OrderHeader> findByPatientIDAndIsArchived(Long patientID,
			String isArchivedStr) {
		QOrderHeader orderHeader = QOrderHeader.orderHeader;
		BooleanExpression exp1 = orderHeader.patient.id.eq(patientID);
		BooleanExpression exp2 = orderHeader.isDeleted.eq("N");
		BooleanExpression exp3 = orderHeader.isArchived.eq(isArchivedStr);
		return Lists.newArrayList(orderHeaderDAO.findAll(exp1.and(exp2).and(
				exp3)));
	}

	@Override
	public List<OrderHeader> findByDoctorIDForPickUp(Long doctorID) {
		QOrderHeader orderHeader = QOrderHeader.orderHeader;
		BooleanExpression exp1 = orderHeader.isDeleted.eq("N");
		BooleanExpression exp2 = orderHeader.isArchived.eq("N");
		BooleanExpression exp3 = orderHeader.status.eq("new");

		BooleanExpression exp4 = orderHeader.doctor.id.eq(doctorID);
		BooleanExpression exp5 = orderHeader.doctor.isNull();
		return Lists.newArrayList(orderHeaderDAO.findAll(exp1.and(exp2)
				.and(exp3).and(exp4.or(exp5))));
	}

	private OrderHeaderCL createOrderHeaderCL(String beforeChange,
			String afterChange, OrderHeader orderHeader, String operationType,
			String operator) {
		OrderHeaderCL ohChangeLog = new OrderHeaderCL();
		ohChangeLog.setCreateTime(new Date());
		ohChangeLog.setType(operationType);
		ohChangeLog.setOwner(operator);
		ohChangeLog.setIsDeleted("N");
		ohChangeLog.setOrderHeader(orderHeader);
		ohChangeLog.setBeforeChange(beforeChange);
		ohChangeLog.setAfterChange(afterChange);
		return orderHeaderCLDAO.save(ohChangeLog);
	}

}
