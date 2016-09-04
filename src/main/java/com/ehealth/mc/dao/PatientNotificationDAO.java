package com.ehealth.mc.dao;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ehealth.mc.bo.PatientNotification;

@Repository
public interface PatientNotificationDAO extends
		CrudRepository<PatientNotification, Long>,
		QueryDslPredicateExecutor<PatientNotification> {

	PatientNotification findOne(Long id);

}