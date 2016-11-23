package com.ehealth.mc.service;

import java.util.List;

import org.apache.olingo.commons.api.data.Entity;

import com.ehealth.mc.bo.OrderBilling;
import com.ehealth.mc.bo.OrderConversation;
import com.ehealth.mc.bo.OrderHeader;

public interface OrderService {

	List<OrderHeader> findAll();

	OrderHeader findById(Long id);

	OrderHeader create(Entity e);

	OrderHeader update(Entity e);

	boolean updateIsDeleted(String value, Long[] objIDs) throws RuntimeException;

	boolean updateIsArchived(String value, Long[] objIDs) throws RuntimeException;

	OrderConversation createOrderConversaction(Entity newEntity, Entity parentEntity);

	OrderConversation createImageOrderConversaction(String fileName, Long orderHeaderID, String owner);

	List<OrderHeader> findByPatientID(Long patientID);

	List<OrderHeader> findByDoctorID(Long doctorID);

	List<OrderHeader> findByDoctorIDForPickUp(Long doctorID);

	List<OrderHeader> findByDoctorIDAndIsArchived(Long doctorID, String isArchivedStr);

	List<OrderHeader> findByPatientIDAndIsArchived(Long patientID, String isArchivedStr);

	OrderBilling createOrderBillingByOrderID(String orderID);

	Integer completeAllNoResponseOrder();

}
