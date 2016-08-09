package com.ehealth.mc.service.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormatUtil {

	public static String FILE_SUFFIX_TIME_FORMATE = "_yyyyMMdd_HHmmss_SSS";

	public static SimpleDateFormat fileSuffixFormater = new SimpleDateFormat(
			FILE_SUFFIX_TIME_FORMATE, Locale.getDefault());

	public static String getFileSuffix() {
		Date date = new Date();
		return fileSuffixFormater.format(date);
	}
	
	
	
	
	
	
}
