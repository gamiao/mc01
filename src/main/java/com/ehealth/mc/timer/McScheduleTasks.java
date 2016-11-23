package com.ehealth.mc.timer;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ehealth.mc.service.MailingService;
import com.ehealth.mc.service.OverallService;
import com.google.gson.Gson;

@Component
@Lazy(value = false)
public class McScheduleTasks {

	@Autowired
	private MailingService mailingService;

	@Autowired
	private OverallService overallService;

	private static Gson gson = new Gson();

	private static final Logger log = LoggerFactory.getLogger(McScheduleTasks.class);

	@Scheduled(fixedDelayString = "${mc.mail.interval}")
	public void handleMailSending() {
		log.info("Sending mail start at: " + new Date());
		mailingService.handleAllPatientMailNotifications();
		mailingService.handleAllDoctorMailNotifications();
		log.info("Sending mail finished at: " + new Date());
	}

	@Scheduled(fixedDelayString = "${mc.order.complete.checkInterval}")
	public void handleCompleteOrder() {
		log.info("Complete order start at: " + new Date());
		overallService.completeAllNoResponseOrder();
		log.info("Complete order finished at: " + new Date());
	}

}
