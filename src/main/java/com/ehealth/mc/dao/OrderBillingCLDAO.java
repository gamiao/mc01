package com.ehealth.mc.dao;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ehealth.mc.bo.OrderBillingCL;

@Repository
public interface OrderBillingCLDAO extends CrudRepository<OrderBillingCL, Long>,
		QueryDslPredicateExecutor<OrderBillingCL> {

	OrderBillingCL findOne(Long id);

}