package com.ehealth.mc.service;

import java.util.List;

import org.apache.olingo.commons.api.data.Entity;

import com.ehealth.mc.bo.OrderConversation;
import com.ehealth.mc.bo.OrderHeader;

public interface OrderService {

	List<OrderHeader> findAll();

	OrderHeader findById(Long id);

	OrderHeader create(Entity e);

	OrderHeader update(Entity e);

	List<OrderHeader> findByPatientID(Long id);

	List<OrderHeader> findByDoctorID(Long id);

	List<OrderHeader> findByDoctorIDForPickUp(Long id);

	List<OrderHeader> findByPatientIDArchived(Long id);

	List<OrderHeader> findByDoctorIDArchived(Long id);

	List<OrderHeader> findByPatientIDNotArchived(Long id);

	List<OrderHeader> findByDoctorIDNotArchived(Long id);

	List<OrderHeader> findByPatientIDAndStatus(Long id, String status);

	List<OrderHeader> findByDoctorIDAndStatus(Long id, String status);

	OrderConversation createOrderConversaction(Entity newEntity,
			Entity parentEntity);

}
