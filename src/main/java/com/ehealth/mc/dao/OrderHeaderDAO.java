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

	@Query("select oh from OrderHeader oh join oh.patient p where (p.id = :patientID)")
	List<OrderHeader> findByPatientID(@Param("patientID") Long patientID);

	@Query("select oh from OrderHeader oh join oh.doctor d where (d.id = :doctorID)")
	List<OrderHeader> findByDoctorID(@Param("doctorID") Long doctorID);

	@Query("select oh from OrderHeader oh join oh.patient p where (p.id = :patientID and oh.status = :status)")
	List<OrderHeader> findByPatientIDAndStatus(
			@Param("patientID") Long patientID, @Param("status") String status);

	@Query("select oh from OrderHeader oh join oh.doctor d where (d.id = :doctorID and oh.status = :status)")
	List<OrderHeader> findByDoctorIDAndStatus(@Param("doctorID") Long doctorID,
			@Param("status") String status);

}
