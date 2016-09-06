package com.ehealth.mc.timer;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ehealth.mc.controller.FileUploadController;
import com.ehealth.mc.service.MailingService;
import com.google.gson.Gson;

@Component
@Lazy(value = false)
public class McScheduleTasks {

	@Autowired
	private MailingService mailingService;

	private static Gson gson = new Gson();

	private static final Logger log = LoggerFactory
			.getLogger(FileUploadController.class);

	@Scheduled(fixedDelayString = "${mc.mail.interval}")
	public void handleMailSending() {
		log.info("Sending mail start at: " + new Date());
		mailingService.handleAllPatientMailNotifications();
		mailingService.handleAllDoctorMailNotifications();
		log.info("Sending mail finished at: " + new Date());
	}

}
