package com.ehealth.mc.dao;

import java.util.List;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ehealth.mc.bo.OrderHeader;

@Repository
public interface OrderHeaderDAO extends CrudRepository<OrderHeader, Long>,
		QueryDslPredicateExecutor<OrderHeader> {

	OrderHeader findOne(Long id);

	List<OrderHeader> findByIsDeleted(String isDeleted);

}
