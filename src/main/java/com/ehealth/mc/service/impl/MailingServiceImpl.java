package com.ehealth.mc.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehealth.mc.bo.DoctorNotification;
import com.ehealth.mc.bo.MailingRecord;
import com.ehealth.mc.bo.PatientNotification;
import com.ehealth.mc.bo.QMailingRecord;
import com.ehealth.mc.dao.MailingRecordDAO;
import com.ehealth.mc.service.MailingService;
import com.ehealth.mc.service.NotificationService;
import com.google.common.collect.Lists;
import com.querydsl.core.types.dsl.BooleanExpression;

@Service("mailingService")
@Transactional
public class MailingServiceImpl implements MailingService {

	@Value("${mc.mail.retry}")
	public int RETRY_TIMES;

	@Value("${mc.mail.sender}")
	public String MAIL_SENDER;

	@Autowired
	MailingRecordDAO mailingRecordDAO;

	@Autowired
	private MailSender mailSender;

	@Autowired
	private NotificationService notificationService;

	@Override
	public void handleAllPatientMailNotifications() {

		List<PatientNotification> availablePatientNotifications = notificationService
				.getAvailablePatientNotifications();

		List<PatientNotification> sendingPatientNotifications = new ArrayList<PatientNotification>();

		for (PatientNotification notification : availablePatientNotifications) {
			if (notification != null) {
				List<MailingRecord> mailingRecords = getMailingRecordsByNotification("P", notification.getId());

				String mailResult = null;

				for (MailingRecord mailingRecord : mailingRecords) {
					if ("S".equals(mailingRecord.getMailResult())) {
						mailResult = "S";
						break;
					}
				}

				if (mailResult == null && mailingRecords.size() >= RETRY_TIMES) {
					mailResult = "E";
				}

				if (mailResult == null) {
					sendingPatientNotifications.add(notification);
				} else {
					notification.setIsDeleted("Y");
					notification.setMailResult(mailResult);
					notificationService.updatePatientNotification(notification);
				}
			}
		}

		for (PatientNotification notification : sendingPatientNotifications) {
			String toMailAddr = notification.getPatient().getMail();
			sendMail(MAIL_SENDER, toMailAddr, notification.getId(), "P", notification.getTitle(),
					notification.getDescription());
		}

	}

	@Override
	public void handleAllDoctorMailNotifications() {

		List<DoctorNotification> availableDoctorNotifications = notificationService.getAvailableDoctorNotifications();

		List<DoctorNotification> sendingDoctorNotifications = new ArrayList<DoctorNotification>();

		for (DoctorNotification notification : availableDoctorNotifications) {
			if (notification != null) {
				List<MailingRecord> mailingRecords = getMailingRecordsByNotification("D", notification.getId());

				String mailResult = null;

				for (MailingRecord mailingRecord : mailingRecords) {
					if ("S".equals(mailingRecord.getMailResult())) {
						mailResult = "S";
						break;
					}
				}

				if (mailResult == null && mailingRecords.size() >= RETRY_TIMES) {
					mailResult = "E";
				}

				if (mailResult == null) {
					sendingDoctorNotifications.add(notification);
				} else {
					notification.setIsDeleted("Y");
					notification.setMailResult(mailResult);
					notificationService.updateDoctorNotification(notification);
				}
			}
		}

		for (DoctorNotification notification : sendingDoctorNotifications) {
			String toMailAddr = notification.getDoctor().getMail();
			sendMail(MAIL_SENDER, toMailAddr, notification.getId(), "D", notification.getTitle(),
					notification.getDescription());
		}

	}

	private List<MailingRecord> getMailingRecordsByNotification(String nType, Long nID) {
		QMailingRecord mailingRecord = QMailingRecord.mailingRecord;
		BooleanExpression exp1 = mailingRecord.notificationID.eq(nID);
		BooleanExpression exp2 = mailingRecord.notificationType.eq(nType);
		return Lists.newArrayList(mailingRecordDAO.findAll(exp1.and(exp2)));
	}

	@Override
	public MailingRecord sendMail(String mailFrom, String mailTo, Long notificationID, String notificationType,
			String subject, String text) {
		String mailResult = "S";
		String mailResultDetail = null;

		JavaMailSender javaMailSender = (JavaMailSender) mailSender;
		MimeMessage mime = javaMailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(mime, false, "utf-8");
			helper.setFrom(mailFrom);
			helper.setTo(mailTo);
			helper.setCc(mailFrom);
			helper.setSubject(subject);
			helper.setText(text);
			javaMailSender.send(mime);
		} catch (Exception e) {
			mailResult = "E";
			mailResultDetail = e.getMessage();
		}

		MailingRecord record = new MailingRecord();
		record.setCreateTime(new Date());
		record.setMailFrom(mailFrom);
		record.setMailTo(mailTo);
		record.setNotificationID(notificationID);
		record.setNotificationType(notificationType);
		record.setIsDeleted("N");
		record.setMailResult(mailResult);
		record.setMailResultDetail(mailResultDetail);
		return mailingRecordDAO.save(record);
	}

}
