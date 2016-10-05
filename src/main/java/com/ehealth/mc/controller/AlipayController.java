package com.ehealth.mc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ehealth.mc.bo.OrderBilling;
import com.ehealth.mc.service.OverallService;

@Controller
public class AlipayController {

	private static final Logger log = LoggerFactory.getLogger(AlipayController.class);

	@Autowired
	private OverallService overallService;

	@RequestMapping(method = RequestMethod.GET, value = "/alipay/getPayForm/{orderID}")
	public @ResponseBody String getPayForm(@PathVariable String orderID) {

		String returnForm = null;
		OrderBilling orderBilling = overallService.createOrderBillingByOrderID(orderID);

		if (orderBilling != null) {
			returnForm = overallService.getPayForm(orderBilling);
		}
		return returnForm;
	}

}
