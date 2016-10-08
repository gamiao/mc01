package com.ehealth.mc.controller;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

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
import com.ehealth.mc.service.util.EntityConvertUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

@Controller
public class AccountController {

	private static Gson gson = new Gson();

	private static final Logger log = LoggerFactory
			.getLogger(AccountController.class);

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private OverallService overallService;

	public static final String[] VALID_LOGIN_TYPES = { "S", "D", "P" };

	@RequestMapping(method = RequestMethod.POST, value = "/login/{loginType}")
	public @ResponseBody String login(@PathVariable String loginType,
			@RequestBody String requestBody, final HttpServletRequest request) {

		String returnMsg = "{\"result\":\"E\"}";
		if (isLoginTypeValid(loginType)) {

			Map<String, String> requestObject = null;

			String ip = request.getRemoteAddr();
			String userAgent = request.getHeader("User-Agent");

			try {
				requestObject = gson.fromJson(requestBody,
						new TypeToken<Map<String, String>>() {
						}.getType());
			} catch (JsonSyntaxException e) {
			}
			if (requestObject != null) {
				String login = requestObject.get("login");
				String password = requestObject.get("password");
				if (login != null && password != null) {
					Long userID = overallService.getLoginUserID(loginType,
							login, password, ip, userAgent);
					if (userID != null) {
						return "{\"result\":\"S\",\"userID\":"
								+ userID.toString() + "}";
					}
				}
			}
		}

		return returnMsg;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/updatePassword/{loginType}")
	public @ResponseBody String updatePassword(@PathVariable String loginType,
			@RequestBody String requestBody) {

		String returnMsg = "{\"result\":\"E\"}";
		if (isLoginTypeValid(loginType)) {

			Map<String, String> requestObject = null;

			try {
				requestObject = gson.fromJson(requestBody,
						new TypeToken<Map<String, String>>() {
						}.getType());
			} catch (JsonSyntaxException e) {
			}
			if (requestObject != null) {
				String idStr = requestObject.get("id");
				Long id = EntityConvertUtil.getLong(idStr);
				String newPassword = requestObject.get("newPassword");
				String oldPassword = requestObject.get("oldPassword");
				if (id != null && oldPassword != null && newPassword != null) {
					Long userID = overallService.updatePassword(loginType, id,
							oldPassword, newPassword);
					if (userID != null) {
						return "{\"result\":\"S\",\"userID\":"
								+ userID.toString() + "}";
					}
				}
			}
		}

		return returnMsg;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/checkLogin/{loginType}")
	public @ResponseBody String checkLogin(@PathVariable String loginType,
			@RequestBody String requestBody) {

		String returnMsg = "{\"result\":\"E\"}";
		if (isLoginTypeValid(loginType)) {

			Map<String, String> requestObject = null;

			try {
				requestObject = gson.fromJson(requestBody,
						new TypeToken<Map<String, String>>() {
						}.getType());
			} catch (JsonSyntaxException e) {
			}
			if (requestObject != null) {
				String login = requestObject.get("login");
				if (login != null
						&& overallService.checkLogin(loginType, login)) {
					return "{\"result\":\"S\"}";
				}
			}
		}

		return returnMsg;
	}

	private boolean isLoginTypeValid(String roleType) {
		for (String i : VALID_LOGIN_TYPES) {
			if (i.equals(roleType)) {
				return true;
			}
		}
		return false;
	}

}
