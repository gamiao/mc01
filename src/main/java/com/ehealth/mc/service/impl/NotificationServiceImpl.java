package com.ehealth.mc.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehealth.mc.bo.Doctor;
import com.ehealth.mc.bo.DoctorNotification;
import com.ehealth.mc.bo.OrderHeader;
import com.ehealth.mc.bo.Patient;
import com.ehealth.mc.bo.PatientNotification;
import com.ehealth.mc.bo.QDoctorNotification;
import com.ehealth.mc.bo.QPatientNotification;
import com.ehealth.mc.dao.DoctorNotificationDAO;
import com.ehealth.mc.dao.PatientNotificationDAO;
import com.ehealth.mc.service.NotificationService;
import com.ehealth.mc.service.util.FormatUtil;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.querydsl.core.types.dsl.BooleanExpression;

@Service("notificationService")
@Transactional
public class NotificationServiceImpl implements NotificationService {

	@Value("${mc.mail.title.prefix}")
	public String mailTitlePrefix;

	private static Gson gson = new Gson();

	@Autowired
	PatientNotificationDAO patientNotificationDAO;

	@Autowired
	DoctorNotificationDAO doctorNotificationDAO;

	@Override
	public PatientNotification notifyPatient(Patient patient, String title,
			String description) {
		if (patient == null) {
			return null;
		}
		PatientNotification notification = new PatientNotification();
		notification.setCreateTime(new Date());
		notification.setPatient(patient);
		notification.setPatientDetail(gson.toJson(patient));
		notification.setTitle(mailTitlePrefix + title);
		notification.setDescription(description + "\n--"
				+ FormatUtil.getMailContentTime());
		notification.setIsDeleted("N");
		return patientNotificationDAO.save(notification);
	}

	@Override
	public DoctorNotification notifyDoctor(Doctor doctor, String title,
			String description) {
		if (doctor == null) {
			return null;
		}
		DoctorNotification notification = new DoctorNotification();
		notification.setCreateTime(new Date());
		notification.setDoctor(doctor);
		notification.setDoctorDetail(gson.toJson(doctor));
		notification.setTitle(mailTitlePrefix + title);
		notification.setDescription(description + "\n--"
				+ FormatUtil.getMailContentTime());
		notification.setIsDeleted("N");
		return doctorNotificationDAO.save(notification);
	}

	@Override
	public List<PatientNotification> getAvailablePatientNotifications() {
		QPatientNotification notification = QPatientNotification.patientNotification;
		BooleanExpression exp1 = notification.isDeleted.eq("N");
		return Lists.newArrayList(patientNotificationDAO.findAll(exp1));
	}

	@Override
	public List<DoctorNotification> getAvailableDoctorNotifications() {
		QDoctorNotification notification = QDoctorNotification.doctorNotification;
		BooleanExpression exp1 = notification.isDeleted.eq("N");
		return Lists.newArrayList(doctorNotificationDAO.findAll(exp1));
	}

	@Override
	public PatientNotification updatePatientNotification(
			PatientNotification notification) {
		return patientNotificationDAO.save(notification);
	}

	@Override
	public DoctorNotification updateDoctorNotification(
			DoctorNotification notification) {
		return doctorNotificationDAO.save(notification);
	}

	@Override
	public boolean notifyForOrder(OrderHeader order, String title,
			String description) {
		Patient patient = order.getPatient();
		Doctor doctor = order.getDoctor();
		PatientNotification pN = notifyPatient(patient, title, description);
		DoctorNotification dN = notifyDoctor(doctor, title, description);
		return pN != null && dN != null;
	}

}
