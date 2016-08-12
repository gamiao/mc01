package com.ehealth.mc.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ehealth.mc.bo.OrderConversation;

@Repository
public interface OrderConversationDAO extends
		CrudRepository<OrderConversation, Long> {

	OrderConversation findOne(Long id);

}
