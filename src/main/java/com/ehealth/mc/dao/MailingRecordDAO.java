package com.ehealth.mc.dao;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ehealth.mc.bo.MailingRecord;

@Repository
public interface MailingRecordDAO extends CrudRepository<MailingRecord, Long>,
		QueryDslPredicateExecutor<MailingRecord> {

	MailingRecord findOne(Long id);

}