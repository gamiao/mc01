package com.ehealth.mc.service;

import com.ehealth.mc.bo.MailingRecord;

public interface MailingService {

	void handleAllPatientMailNotifications();

	void handleAllDoctorMailNotifications();

	MailingRecord sendMail(String mailFrom, String mailTo, Long notificationID,
			String notificationType, String subject, String text);

}
