package com.ehealth.mc.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ehealth.mc.bo.OrderDetail;

@Repository
public interface OrderDetailDAO extends CrudRepository<OrderDetail, Integer> {

	List<OrderDetail> findById(Integer id);

}
