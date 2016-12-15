package com.ehealth.mc.controller;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ehealth.mc.service.OverallService;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

@Controller
public class OrderController {

	private static Gson gson = new Gson();

	private static final Logger log = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private OverallService overallService;

	public static final String[] VALID_ACTION_TYPES = { "setIsDeleted", "setIsArchived", "reopenOrders",
			"completeOrders" };

	@RequestMapping(method = RequestMethod.POST, value = "/O/{actionType}")
	public @ResponseBody String entityAction(@PathVariable String actionType, @RequestBody String requestBody) {

		String returnMsg = "{\"result\":\"E\"}";
		if (isActionTypeValid(actionType)) {
			if ("setIsDeleted".equals(actionType)) {
				String result = setIsDeleted(requestBody);
				if (result != null) {
					return result;
				}
			} else if ("setIsArchived".equals(actionType)) {
				String result = setIsArchived(requestBody);
				if (result != null) {
					return result;
				}
			} else if ("reopenOrders".equals(actionType)) {
				String result = reopenOrders(requestBody);
				if (result != null) {
					return result;
				}
			} else if ("completeOrders".equals(actionType)) {
				String result = completeOrders(requestBody);
				if (result != null) {
					return result;
				}
			}
		}
		return returnMsg;
	}

	public String reopenOrders(String requestBody) {
		BooleanValueObjIDs obj = null;
		try {
			obj = gson.fromJson(requestBody, BooleanValueObjIDs.class);
		} catch (JsonSyntaxException e) {
		}
		if (obj != null && obj.value != null && obj.objectIDs != null && obj.objectIDs.length > 0) {
			if ("reopen".equals(obj.value)) {
				boolean result = overallService.reopenOrders(obj.objectIDs);
				if (result) {
					return "{\"result\":\"S\"}";
				}
			}
		}
		return null;
	}

	public String completeOrders(String requestBody) {
		BooleanValueObjIDs obj = null;
		try {
			obj = gson.fromJson(requestBody, BooleanValueObjIDs.class);
		} catch (JsonSyntaxException e) {
		}
		if (obj != null && obj.value != null && obj.objectIDs != null && obj.objectIDs.length > 0) {
			if ("complete".equals(obj.value)) {
				boolean result = overallService.completeOrders(obj.objectIDs);
				if (result) {
					return "{\"result\":\"S\"}";
				}
			}
		}
		return null;
	}

	public String setIsDeleted(String requestBody) {
		BooleanValueObjIDs obj = null;
		try {
			obj = gson.fromJson(requestBody, BooleanValueObjIDs.class);
		} catch (JsonSyntaxException e) {
		}
		if (obj != null && obj.value != null && obj.objectIDs != null && obj.objectIDs.length > 0) {
			if ("Y".equals(obj.value) || "N".equals(obj.value)) {
				boolean result = overallService.setIsDeleted("Order", obj.value, obj.objectIDs);
				if (result) {
					return "{\"result\":\"S\"}";
				}
			}
		}

		return null;
	}

	public String setIsArchived(String requestBody) {
		BooleanValueObjIDs obj = null;
		try {
			obj = gson.fromJson(requestBody, BooleanValueObjIDs.class);
		} catch (JsonSyntaxException e) {
		}
		if (obj != null && obj.value != null && obj.objectIDs != null && obj.objectIDs.length > 0) {
			if ("Y".equals(obj.value) || "N".equals(obj.value)) {
				boolean result = overallService.setIsArchived("Order", obj.value, obj.objectIDs);
				if (result) {
					return "{\"result\":\"S\"}";
				}
			}
		}

		return null;
	}

	private boolean isActionTypeValid(String actionType) {
		for (String i : VALID_ACTION_TYPES) {
			if (i.equals(actionType)) {
				return true;
			}
		}
		return false;
	}

	class BooleanValueObjIDs {
		String value;
		Long[] objectIDs;
	}

}
