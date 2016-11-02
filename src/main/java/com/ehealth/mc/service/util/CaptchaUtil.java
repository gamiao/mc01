package com.ehealth.mc.service.util;

import java.util.HashMap;
import java.util.Map;

public class CaptchaUtil {

	private static Map<String, String> dataMap = new HashMap<String, String>();

	public static void store(String key, String value) {
		dataMap.put(key, value);
	}

	public static boolean validate(String key, String value) {
		String result = dataMap.get(key);
		if (result != null && result.equals(value)) {
			dataMap.remove(key);
			return true;
		}
		return false;
	}
}