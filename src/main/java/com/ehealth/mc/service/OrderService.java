package com.ehealth.mc.service;

import java.util.List;

import org.apache.olingo.commons.api.data.Entity;

import com.ehealth.mc.bo.OrderHeader;

public interface OrderService {

	List<OrderHeader> findAll();

	OrderHeader findById(Long id);

	OrderHeader save(Entity e);

	List<OrderHeader> findByPatientID(Long patientID);

	List<OrderHeader> findByDoctorID(Long doctorID);

}
