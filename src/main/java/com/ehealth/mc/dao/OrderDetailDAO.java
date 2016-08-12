package com.ehealth.mc.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ehealth.mc.bo.OrderDetail;

@Repository
public interface OrderDetailDAO extends CrudRepository<OrderDetail, Long> {

	OrderDetail findOne(Long id);

}
