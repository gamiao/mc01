package com.ehealth.mc.dao;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ehealth.mc.bo.OrderHeaderCL;

@Repository
public interface OrderHeaderCLDAO extends CrudRepository<OrderHeaderCL, Long>,
		QueryDslPredicateExecutor<OrderHeaderCL> {

	OrderHeaderCL findOne(Long id);

}