package com.ehealth.mc.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ehealth.mc.bo.OrderConversation;

@Repository
public interface OrderConversationDAO extends
		CrudRepository<OrderConversation, Integer> {

	List<OrderConversation> findById(Long id);

}
