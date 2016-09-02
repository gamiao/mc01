package com.ehealth.mc.dao;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ehealth.mc.bo.LoginLog;

@Repository
public interface LoginLogDAO extends CrudRepository<LoginLog, Long>,
		QueryDslPredicateExecutor<LoginLog> {

	LoginLog findOne(Long id);

}
