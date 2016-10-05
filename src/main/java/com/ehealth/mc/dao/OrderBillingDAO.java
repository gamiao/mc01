package com.ehealth.mc.dao;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ehealth.mc.bo.OrderBilling;

@Repository
public interface OrderBillingDAO extends CrudRepository<OrderBilling, Long>, QueryDslPredicateExecutor<OrderBilling> {

	OrderBilling findOne(Long id);

}