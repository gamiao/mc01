package com.ehealth.mc.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ehealth.mc.bo.OrderHeader;

@Repository
public interface OrderHeaderDAO extends CrudRepository<OrderHeader, Integer> {

	List<OrderHeader> findById(Long id);

	List<OrderHeader> findByStatusAndIsArchivedAndIsEnabledAndIsDeleted(
			String status, String isArchived, String isEnabled, String isDeleted);

	@Query("select oh from OrderHeader oh join oh.patient p where (p.id = :id)")
	List<OrderHeader> findByPatientID(@Param("id") Long id);

	@Query("select oh from OrderHeader oh join oh.doctor d where (d.id = :id)")
	List<OrderHeader> findByDoctorID(@Param("id") Long id);

	@Query("select oh from OrderHeader oh join oh.patient p where oh.isArchived = 'Y' and (p.id = :id)")
	List<OrderHeader> findByPatientIDArchived(@Param("id") Long id);

	@Query("select oh from OrderHeader oh join oh.doctor d where oh.isArchived = 'Y' and (d.id = :id)")
	List<OrderHeader> findByDoctorIDArchived(@Param("id") Long id);

	@Query("select oh from OrderHeader oh join oh.patient p where oh.isArchived != 'Y' and (p.id = :id)")
	List<OrderHeader> findByPatientIDNotArchived(@Param("id") Long id);

	@Query("select oh from OrderHeader oh join oh.doctor d where oh.isArchived != 'Y' and (d.id = :id)")
	List<OrderHeader> findByDoctorIDNotArchived(@Param("id") Long id);

	@Query("select oh from OrderHeader oh join oh.patient p where (p.id = :id and oh.status = :status)")
	List<OrderHeader> findByPatientIDAndStatus(@Param("id") Long id,
			@Param("status") String status);

	@Query("select oh from OrderHeader oh join oh.doctor d where (d.id = :id and oh.status = :status)")
	List<OrderHeader> findByDoctorIDAndStatus(@Param("id") Long id,
			@Param("status") String status);

}
