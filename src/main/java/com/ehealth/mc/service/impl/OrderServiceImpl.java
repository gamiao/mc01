package com.ehealth.mc.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.olingo.commons.api.data.ComplexValue;
import org.apache.olingo.commons.api.data.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehealth.mc.bo.Doctor;
import com.ehealth.mc.bo.OrderBilling;
import com.ehealth.mc.bo.OrderBillingCL;
import com.ehealth.mc.bo.OrderConversation;
import com.ehealth.mc.bo.OrderDetail;
import com.ehealth.mc.bo.OrderHeader;
import com.ehealth.mc.bo.OrderHeaderCL;
import com.ehealth.mc.bo.Patient;
import com.ehealth.mc.bo.QOrderHeader;
import com.ehealth.mc.common.JsonAnnotationExclusionStrategy;
import com.ehealth.mc.dao.OrderBillingCLDAO;
import com.ehealth.mc.dao.OrderBillingDAO;
import com.ehealth.mc.dao.OrderConversationDAO;
import com.ehealth.mc.dao.OrderDetailDAO;
import com.ehealth.mc.dao.OrderHeaderCLDAO;
import com.ehealth.mc.dao.OrderHeaderDAO;
import com.ehealth.mc.service.DoctorService;
import com.ehealth.mc.service.NotificationService;
import com.ehealth.mc.service.OrderService;
import com.ehealth.mc.service.PatientService;
import com.ehealth.mc.service.util.EntityConvertUtil;
import com.ehealth.mc.service.util.FormatUtil;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;

@Service("orderService")
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

	@Value("${mc.alipay.subject}")
	public String ALIPAY_PAY_API_SUBJECT;

	@Value("${mc.alipay.description}")
	public String ALIPAY_PAY_API_DESCRIPTION;

	@Value("${mc.alipay.product_code}")
	public String ALIPAY_PAY_API_PRODUCT_CODE;

	@Value("${mc.order.complete.silentDay}")
	public Integer COMPLETE_ORDER_SILENT_DAY;

	private static JsonAnnotationExclusionStrategy strategy = new JsonAnnotationExclusionStrategy();

	private static Gson gson = new GsonBuilder().addDeserializationExclusionStrategy(strategy)
			.addSerializationExclusionStrategy(strategy).create();

	@Autowired
	private OrderConversationDAO orderConversationDAO;

	@Autowired
	private OrderHeaderDAO orderHeaderDAO;

	@Autowired
	private OrderHeaderCLDAO orderHeaderCLDAO;

	@Autowired
	private OrderBillingDAO orderBillingDAO;

	@Autowired
	private OrderBillingCLDAO orderBillingCLDAO;

	@Autowired
	private OrderDetailDAO orderDetailDAO;

	@Autowired
	private DoctorService doctorService;

	@Autowired
	private PatientService patientService;

	@Autowired
	private NotificationService notificationService;

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

		OrderHeader orderHeader = createOrderHeader(e, patient, doctor, orderDetail);

		if (orderHeader == null) {
			throw new RuntimeException("Can't create orderHeader object.");
		}

		String nTitle = "新咨询(编号：" + orderHeader.getId() + ")已经建立";
		String nDescription = "新咨询已经成功创建，请登录咨询平台并查看详情。";
		notificationService.notifyForOrder(orderHeader, nTitle, nDescription);
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

		Long orderDetailID = EntityConvertUtil.getOrderDetailIDFromOrderEntity(e);
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

	private OrderHeader updateOrderHeader(Entity e, Patient patient, Doctor doctor, OrderDetail orderDetail,
			OrderHeader orderHeader) {

		String beforeChange = gson.toJson(orderHeader);
		EntityConvertUtil.updateOrderHeader(orderHeader, e);

		String afterStatus = orderHeader.getStatus();
		orderHeader.setDoctor(doctor);
		// TODO need to know who make this change.

		orderHeaderDAO.save(orderHeader);
		String afterChange = gson.toJson(orderHeader);
		String operator = "S";
		String operationType = "UPDATE";
		createOrderHeaderCL(beforeChange, afterChange, orderHeader, operationType, operator);

		String nTitle = "咨询(编号：" + orderHeader.getId() + ")的状态有更新";
		String nDescription = "咨询已经有新的变动，请登录咨询平台并查看详情。";

		if (afterStatus.equals("reject")) {
			nDescription = "咨询已被医师拒接，请在历史列表查看。";
		} else if (afterStatus.equals("complete")) {
			nDescription = "咨询已被医师置为完成，请在历史列表查看。";
		}

		notificationService.notifyForOrder(orderHeader, nTitle, nDescription);

		return orderHeader;
	}

	private OrderHeader createOrderHeader(Entity e, Patient patient, Doctor doctor, OrderDetail orderDetail) {
		OrderHeader orderHeader = EntityConvertUtil.getOrderHeader(e);
		if (orderHeader != null) {
			orderHeader.setDoctor(doctor);
			orderHeader.setPatient(patient);
			orderHeader.setOrderDetail(orderDetail);
			OrderHeader result = orderHeaderDAO.save(orderHeader);
			createOrderHeaderCL(null, gson.toJson(orderHeader), result, "CREATE", "P");
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
	public OrderConversation createOrderConversaction(Entity newEntity, Entity parentEntity) {

		Long orderHeaderID = EntityConvertUtil.getID(parentEntity);
		OrderHeader orderHeader = findById(orderHeaderID);

		if (orderHeader != null) {
			OrderConversation oc = EntityConvertUtil.getOrderConversation(newEntity);
			oc.setOrderHeader(orderHeader);
			orderConversationDAO.save(oc);

			if (oc.getOwner().equals("D")) {
				String nTitle = "咨询(编号：" + orderHeader.getId() + ")有新的留言";
				String nDescription = "咨询已经有新的变动，请登录咨询平台并查看详情。";
				notificationService.notifyPatient(orderHeader.getPatient(), nTitle, nDescription);
			} else if (oc.getOwner().equals("P")) {
				String nTitle = "咨询(编号：" + orderHeader.getId() + ")有新的留言";
				String nDescription = "咨询已经有新的变动，请登录咨询平台并查看详情。";
				notificationService.notifyDoctor(orderHeader.getDoctor(), nTitle, nDescription);
			}

			List<OrderConversation> convs = orderHeader.getOrderConversations();
			int doctorTextConvNumber = 0;
			for (OrderConversation conv : convs) {
				if (conv != null && "D".equals(conv.getOwner()) && "TEXT".equals(conv.getType())) {
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
				createOrderHeaderCL(beforeChange, afterChange, orderHeader, operationType, operator);

				String nTitle = "咨询(编号：" + orderHeader.getId() + ")的已经完结";
				String nDescription = "医师已经答复三次，咨询平台自动完结本咨询，请登录咨询平台并查看详情。";
				notificationService.notifyForOrder(orderHeader, nTitle, nDescription);

			}

			return oc;
		}
		return null;
	}

	@Override
	@Transactional
	public OrderConversation createImageOrderConversaction(String fileName, Long orderHeaderID, String owner) {

		OrderHeader orderHeader = findById(orderHeaderID);

		if (orderHeader != null) {
			OrderConversation oc = new OrderConversation();
			oc.setCreateTime(new Date());
			oc.setType("IMAGE");
			oc.setOwner(owner);
			oc.setOrderHeader(orderHeader);
			oc.setPictures(fileName);
			orderConversationDAO.save(oc);

			if (oc.getOwner().equals("D")) {
				String nTitle = "咨询(编号：" + orderHeader.getId() + ")有新的图片消息";
				String nDescription = "咨询已经有新的变动，请登录咨询平台并查看详情。";
				notificationService.notifyPatient(orderHeader.getPatient(), nTitle, nDescription);
			} else if (oc.getOwner().equals("P")) {
				String nTitle = "咨询(编号：" + orderHeader.getId() + ")有新的图片消息";
				String nDescription = "咨询已经有新的变动，请登录咨询平台并查看详情。";
				notificationService.notifyDoctor(orderHeader.getDoctor(), nTitle, nDescription);
			}
			return oc;
		}
		return null;
	}

	@Override
	@Transactional(rollbackFor = RuntimeException.class)
	public boolean reopenOrders(Long[] objIDs) throws RuntimeException {
		for (Long id : objIDs) {
			if (id == null) {
				throw new RuntimeException("Some id is empty!");
			}
			OrderHeader obj = orderHeaderDAO.findOne(id);
			if (obj == null) {
				throw new RuntimeException("Can not find the object with ID:" + id);
			}
			if (!"complete".equals(obj.getStatus())) {
				throw new RuntimeException("The order status is not complete, ID:" + id);
			}
			String beforeChange = gson.toJson(obj);
			OrderConversation oc = new OrderConversation();
			oc.setCreateTime(new Date());
			oc.setType("TEXT");
			oc.setOwner("S");
			oc.setOrderHeader(obj);
			oc.setTitle("本订单已被管理员重启");
			oc.setDescription("本订单已被管理员重启");
			orderConversationDAO.save(oc);

			obj.setStatus("ongoing");
			obj.setIsArchived("N");
			String afterChange = gson.toJson(obj);

			String operator = "S";
			String operationType = "UPDATE";

			try {
				orderHeaderDAO.save(obj);
				createOrderHeaderCL(beforeChange, afterChange, obj, operationType, operator);
				String nTitle = "咨询(编号：" + obj.getId() + ")的状态有更新";
				String nDescription = "咨询已被重启，请登录咨询平台并查看详情。";

				if (nDescription != null) {
					notificationService.notifyForOrder(obj, nTitle, nDescription);
				}
			} catch (Exception e) {
				throw new RuntimeException("Can't update the object with ID:" + id);
			}
		}
		return true;
	}

	@Override
	@Transactional(rollbackFor = RuntimeException.class)
	public boolean completeOrders(Long[] objIDs) throws RuntimeException {
		for (Long id : objIDs) {
			if (id == null) {
				throw new RuntimeException("Some id is empty!");
			}
			OrderHeader obj = orderHeaderDAO.findOne(id);
			if (obj == null) {
				throw new RuntimeException("Can not find the object with ID:" + id);
			}
			if (!"ongoing".equals(obj.getStatus())) {
				throw new RuntimeException("The order status is not ongoing, ID:" + id);
			}
			String beforeChange = gson.toJson(obj);
			OrderConversation oc = new OrderConversation();
			oc.setCreateTime(new Date());
			oc.setType("TEXT");
			oc.setOwner("S");
			oc.setOrderHeader(obj);
			oc.setTitle("本订单已被管理员关闭");
			oc.setDescription("本订单已被管理员关闭");
			orderConversationDAO.save(oc);

			obj.setStatus("complete");
			obj.setIsArchived("Y");
			String afterChange = gson.toJson(obj);

			String operator = "S";
			String operationType = "UPDATE";

			try {
				orderHeaderDAO.save(obj);
				createOrderHeaderCL(beforeChange, afterChange, obj, operationType, operator);
				String nTitle = "咨询(编号：" + obj.getId() + ")的状态有更新";
				String nDescription = "咨询已被关闭，请登录咨询平台并查看详情。";

				if (nDescription != null) {
					notificationService.notifyForOrder(obj, nTitle, nDescription);
				}
			} catch (Exception e) {
				throw new RuntimeException("Can't update the object with ID:" + id);
			}
		}
		return true;
	}

	@Override
	@Transactional(rollbackFor = RuntimeException.class)
	public boolean updateIsDeleted(String value, Long[] objIDs) throws RuntimeException {
		for (Long id : objIDs) {
			if (id == null) {
				throw new RuntimeException("Some id is empty!");
			}
			OrderHeader obj = orderHeaderDAO.findOne(id);
			if (obj == null) {
				throw new RuntimeException("Can not find the object with ID:" + id);
			}
			String beforeChange = gson.toJson(obj);
			obj.setIsDeleted(value);
			String afterChange = gson.toJson(obj);

			String operator = "S";
			String operationType = "UPDATE";

			try {
				orderHeaderDAO.save(obj);
				createOrderHeaderCL(beforeChange, afterChange, obj, operationType, operator);
				String nTitle = "咨询(编号：" + obj.getId() + ")的状态有更新";
				String nDescription = null;

				if ("Y".equals(value)) {
					nDescription = "咨询已被删除，请登录咨询平台并查看详情。";
				} else if ("N".equals(value)) {
					nDescription = "咨询已经回复为正常咨询，请登录咨询平台并查看详情。";
				}
				if (nDescription != null) {
					notificationService.notifyForOrder(obj, nTitle, nDescription);
				}
			} catch (Exception e) {
				throw new RuntimeException("Can't update the object with ID:" + id);
			}
		}
		return true;
	}

	@Override
	@Transactional(rollbackFor = RuntimeException.class)
	public boolean updateIsArchived(String value, Long[] objIDs) throws RuntimeException {
		for (Long id : objIDs) {
			if (id == null) {
				throw new RuntimeException("Some id is empty!");
			}
			OrderHeader obj = orderHeaderDAO.findOne(id);
			if (obj == null) {
				throw new RuntimeException("Can not find the object with ID:" + id);
			}
			String beforeChange = gson.toJson(obj);
			obj.setIsArchived(value);
			String afterChange = gson.toJson(obj);

			String operator = "S";
			String operationType = "UPDATE";

			try {
				orderHeaderDAO.save(obj);
				createOrderHeaderCL(beforeChange, afterChange, obj, operationType, operator);

				String nTitle = "咨询(编号：" + obj.getId() + ")的状态有更新";
				String nDescription = null;

				if ("Y".equals(value)) {
					nDescription = "咨询已经转为历史咨询，请登录咨询平台并查看详情。";
				} else if ("N".equals(value)) {
					nDescription = "咨询已经回复为正常咨询，请登录咨询平台并查看详情。";
				}
				if (nDescription != null) {
					notificationService.notifyForOrder(obj, nTitle, nDescription);
				}

			} catch (Exception e) {
				throw new RuntimeException("Can't update the object with ID:" + id);
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
	public List<OrderHeader> findByDoctorIDAndIsArchived(Long doctorID, String isArchivedStr) {
		QOrderHeader orderHeader = QOrderHeader.orderHeader;
		BooleanExpression exp1 = orderHeader.doctor.id.eq(doctorID);
		BooleanExpression exp2 = orderHeader.isDeleted.eq("N");
		BooleanExpression exp3 = orderHeader.isArchived.eq(isArchivedStr);
		BooleanExpression exp4 = orderHeader.status.ne("new");
		return Lists.newArrayList(orderHeaderDAO.findAll(exp1.and(exp2).and(exp3).and(exp4)));
	}

	@Override
	public List<OrderHeader> findByPatientIDAndIsArchived(Long patientID, String isArchivedStr) {
		QOrderHeader orderHeader = QOrderHeader.orderHeader;
		BooleanExpression exp1 = orderHeader.patient.id.eq(patientID);
		BooleanExpression exp2 = orderHeader.isDeleted.eq("N");
		BooleanExpression exp3 = orderHeader.isArchived.eq(isArchivedStr);
		return Lists.newArrayList(orderHeaderDAO.findAll(exp1.and(exp2).and(exp3)));
	}

	@Override
	public List<OrderHeader> findByDoctorIDForPickUp(Long doctorID) {
		QOrderHeader orderHeader = QOrderHeader.orderHeader;
		BooleanExpression exp1 = orderHeader.isDeleted.eq("N");
		BooleanExpression exp2 = orderHeader.isArchived.eq("N");
		BooleanExpression exp3 = orderHeader.status.eq("new");

		BooleanExpression exp4 = orderHeader.doctor.id.eq(doctorID);
		BooleanExpression exp5 = orderHeader.doctor.isNull();
		return Lists.newArrayList(orderHeaderDAO.findAll(exp1.and(exp2).and(exp3).and(exp4.or(exp5))));
	}

	@Transactional
	private OrderHeaderCL createOrderHeaderCL(String beforeChange, String afterChange, OrderHeader orderHeader,
			String operationType, String operator) {
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

	@Override
	@Transactional
	public OrderBilling createOrderBillingByOrderID(String orderID) {

		Long orderHeaderID = EntityConvertUtil.getLong(orderID);

		if (orderHeaderID != null) {
			OrderHeader orderHeader = findById(orderHeaderID);

			if (orderHeader != null) {
				OrderBilling orderBilling = new OrderBilling();
				Date currentTime = new Date();

				orderBilling.setCreateTime(currentTime);
				orderBilling.setIsDeleted("N");
				orderBilling.setChannel("alipay");
				orderBilling.setOrderHeader(orderHeader);

				// Order value
				orderBilling.setPrice(orderHeader.getDoctor().getPrice().doubleValue());
				orderBilling.setBillingCode(FormatUtil.getBillingCode(currentTime, orderHeader.getId()));
				orderBilling.setProductCode(ALIPAY_PAY_API_PRODUCT_CODE);
				orderBilling.setTitle(ALIPAY_PAY_API_SUBJECT);
				orderBilling.setDescription(ALIPAY_PAY_API_DESCRIPTION);

				OrderBilling result = orderBillingDAO.save(orderBilling);

				String afterChange = null;
				try {
					afterChange = gson.toJson(result);
				} catch (Exception e) {

				}
				createOrderBillingCL(null, afterChange, result, "CREATE", "P");

				return result;
			}
		}
		return null;
	}

	private OrderBillingCL createOrderBillingCL(String beforeChange, String afterChange, OrderBilling orderBilling,
			String operationType, String operator) {
		OrderBillingCL obChangeLog = new OrderBillingCL();
		obChangeLog.setCreateTime(new Date());
		obChangeLog.setType(operationType);
		obChangeLog.setOwner(operator);
		obChangeLog.setIsDeleted("N");
		obChangeLog.setOrderBilling(orderBilling);
		obChangeLog.setBeforeChange(beforeChange);
		obChangeLog.setAfterChange(afterChange);
		return orderBillingCLDAO.save(obChangeLog);
	}

	@Override
	@Transactional
	public Integer completeAllNoResponseOrder() {
		Date currentTime = new Date();
		Date checkDateBefore = new Date(currentTime.getTime() - COMPLETE_ORDER_SILENT_DAY * 24 * 3600 * 1000);
		QOrderHeader qOrderHeader = QOrderHeader.orderHeader;
		BooleanExpression exp1 = qOrderHeader.status.eq("ongoing");
		List<OrderHeader> ongoingOrders = Lists.newArrayList(orderHeaderDAO.findAll(exp1));
		for (OrderHeader oh : ongoingOrders) {
			if (oh != null && "ongoing".equals(oh.getStatus()) && oh.getOrderConversations() != null) {
				List<OrderConversation> convs = oh.getOrderConversations();
				OrderConversation latestConv = null;
				Date latestDate = FormatUtil.getParsedDate("1970-01-01");
				for (OrderConversation conv : convs) {
					Date convDate = conv.getCreateTime();
					if (latestDate.before(convDate)) {
						latestDate = convDate;
						latestConv = conv;
					}
				}
				if (latestConv != null && latestConv.getOwner().equals("D") && latestConv.getCreateTime() != null) {
					Date convTime = latestConv.getCreateTime();
					if (convTime.before(checkDateBefore)) {
						String beforeChange = gson.toJson(oh);
						oh.setStatus("complete");
						oh.setIsArchived("Y");
						String afterChange = gson.toJson(oh);
						String operator = "S";
						String operationType = "UPDATE";
						orderHeaderDAO.save(oh);
						createOrderHeaderCL(beforeChange, afterChange, oh, operationType, operator);

						String nTitle = "咨询(编号：" + oh.getId() + ")的已经完结";
						String nDescription = "医师答复已过" + COMPLETE_ORDER_SILENT_DAY
								+ "天等待时间，咨询平台自动完结本咨询，请登录咨询平台并在历史订单中查看。";
						notificationService.notifyForOrder(oh, nTitle, nDescription);
					}
				}
			}
		}
		return null;
	}

	@Override
	public List<OrderHeader> findByIsDeleted(String isDeleted) {
		return orderHeaderDAO.findByIsDeleted(isDeleted);
	}

}
