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
public class PatientController {

	private static Gson gson = new Gson();

	private static final Logger log = LoggerFactory
			.getLogger(FileUploadController.class);

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private OverallService overallService;

	public static final String[] VALID_ACTION_TYPES = { "setIsDeleted" };

	@RequestMapping(method = RequestMethod.POST, value = "/P/{actionType}")
	public @ResponseBody String entityAction(@PathVariable String actionType,
			@RequestBody String requestBody) {

		String returnMsg = "{\"result\":\"E\"}";
		if (isActionTypeValid(actionType)) {
			if ("setIsDeleted".equals(actionType)) {
				String result = setIsDeleted(requestBody);
				if (result != null) {
					return result;
				}
			}
		}
		return returnMsg;
	}

	public String setIsDeleted(String requestBody) {
		BooleanValueObjIDs obj = null;
		try {
			obj = gson.fromJson(requestBody, BooleanValueObjIDs.class);
		} catch (JsonSyntaxException e) {
		}
		if (obj != null && obj.value != null && obj.objectIDs != null
				&& obj.objectIDs.length > 0) {
			if ("Y".equals(obj.value) || "N".equals(obj.value)) {
				boolean result = overallService.setIsDeleted("Patient",
						obj.value, obj.objectIDs);
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
