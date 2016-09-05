package com.ehealth.mc.service;

import java.util.List;

import com.ehealth.mc.bo.Doctor;
import com.ehealth.mc.bo.DoctorNotification;
import com.ehealth.mc.bo.OrderHeader;
import com.ehealth.mc.bo.Patient;
import com.ehealth.mc.bo.PatientNotification;

public interface NotificationService {

	PatientNotification notifyPatient(Patient patient, String title,
			String description);

	DoctorNotification notifyDoctor(Doctor doctor, String title,
			String description);

	boolean notifyForOrder(OrderHeader order, String title, String description);

	List<PatientNotification> getAvailablePatientNotifications();

	List<DoctorNotification> getAvailableDoctorNotifications();

	PatientNotification updatePatientNotification(
			PatientNotification notification);

	DoctorNotification updateDoctorNotification(DoctorNotification notification);

}
