package com.ehealth.mc.dao;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ehealth.mc.bo.DoctorNotification;

@Repository
public interface DoctorNotificationDAO extends
		CrudRepository<DoctorNotification, Long>,
		QueryDslPredicateExecutor<DoctorNotification> {

	DoctorNotification findOne(Long id);

}