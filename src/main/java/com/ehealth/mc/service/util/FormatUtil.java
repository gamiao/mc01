package com.ehealth.mc.service.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatUtil {

	public static String FILE_SUFFIX_TIME_FORMATE = "_yyyyMMdd_HHmmss_SSS";

	public static SimpleDateFormat fileSuffixFormater = new SimpleDateFormat(
			FILE_SUFFIX_TIME_FORMATE, Locale.getDefault());

	public static String getFileSuffix() {
		Date date = new Date();
		return fileSuffixFormater.format(date);
	}

	public static Long getOrderFilterPatientID(String filterText) {
		if (filterText != null) {
			Pattern fullPattern = Pattern.compile("CTPatient/ID eq \\d+");
			Matcher fullMatcher = fullPattern.matcher(filterText);
			if (fullMatcher.find()) {
				String subFilterText = filterText.substring(
						fullMatcher.start(), fullMatcher.end());
				Pattern numberPattern = Pattern.compile("\\d+");
				Matcher numberMatcher = numberPattern.matcher(subFilterText);
				if (numberMatcher.find()) {
					int beginIndex = numberMatcher.start();
					int endIndex = numberMatcher.end();
					String numberStr = subFilterText.substring(beginIndex,
							endIndex);
					return EntityConvertUtil.getLong(numberStr);
				}
			}
		}
		return null;
	}

	public static Long getOrderFilterDoctorID(String filterText) {
		if (filterText != null) {
			Pattern fullPattern = Pattern.compile("CTDoctor/ID eq \\d+");
			Matcher fullMatcher = fullPattern.matcher(filterText);
			if (fullMatcher.find()) {
				String subFilterText = filterText.substring(
						fullMatcher.start(), fullMatcher.end());

				Pattern numberPattern = Pattern.compile("\\d+");
				Matcher numberMatcher = numberPattern.matcher(subFilterText);
				if (numberMatcher.find()) {
					int beginIndex = numberMatcher.start();
					int endIndex = numberMatcher.end();
					String numberStr = subFilterText.substring(beginIndex,
							endIndex);
					return EntityConvertUtil.getLong(numberStr);
				}
			}
		}
		return null;
	}

	public static String getOrderFilterStatus(String filterText) {
		if (filterText != null) {
			Pattern fullPattern = Pattern.compile("Status eq '[a-zA-Z0-9_-]+'");
			Matcher fullMatcher = fullPattern.matcher(filterText);
			if (fullMatcher.find()) {
				String subFilterText = filterText.substring(
						fullMatcher.start(), fullMatcher.end());
				int beginIndex = subFilterText.indexOf("'") + 1;
				int endIndex = subFilterText.length() - 1;

				String resultStr = subFilterText
						.substring(beginIndex, endIndex);
				return resultStr;
			}
		}
		return null;
	}

	public static String getCommonFilterString(String filterText) {
		if (filterText != null) {
			Pattern fullPattern = Pattern
					.compile("Address eq '[\u4E00-\u9FA5A-Za-z0-9|\\s|\\S]+'");
			Matcher fullMatcher = fullPattern.matcher(filterText);
			if (fullMatcher.find()) {
				String subFilterText = filterText.substring(
						fullMatcher.start(), fullMatcher.end());
				int beginIndex = subFilterText.indexOf("'") + 1;
				int endIndex = subFilterText.length() - 1;

				String resultStr = subFilterText
						.substring(beginIndex, endIndex);
				return resultStr;
			}
		}
		return null;
	}

	public static String getOrderFilterIsArchived(String filterText) {
		if (filterText != null) {
			Pattern fullPattern = Pattern
					.compile("IsArchived eq '[a-zA-Z0-9_-]+'");
			Matcher fullMatcher = fullPattern.matcher(filterText);
			if (fullMatcher.find()) {
				String subFilterText = filterText.substring(
						fullMatcher.start(), fullMatcher.end());
				int beginIndex = subFilterText.indexOf("'") + 1;
				int endIndex = subFilterText.length() - 1;
				String resultStr = subFilterText
						.substring(beginIndex, endIndex);
				return resultStr;
			}
		}
		return null;
	}

	public static void main(String[] args) {
		String result = getOrderFilterStatus("CTDoctor/ID eq 1234) and (Status eq 'test123'");
		System.out.println(result);
	}

}
