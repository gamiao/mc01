package com.ehealth.mc.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ehealth.mc.bo.OrderHeader;

@Repository
public interface OrderHeaderDAO extends CrudRepository<OrderHeader, Integer> {

	List<OrderHeader> findById(Long id);

	List<OrderHeader> findByStatusAndIsArchivedAndIsEnabledAndIsDeleted(
			String status, String isArchived, String isEnabled, String isDeleted);

}
