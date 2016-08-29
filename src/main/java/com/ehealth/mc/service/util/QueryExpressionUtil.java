package com.ehealth.mc.service.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ehealth.mc.bo.QDoctor;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

public class QueryExpressionUtil {

	public static Predicate getDoctorWhereClausesByFilterString(
			String filterString) {
		if (filterString == null || filterString.isEmpty()) {
			return null;
		}

		List<BooleanExpression> clauses = new ArrayList<BooleanExpression>();
		QDoctor doctor = QDoctor.doctor;
		String[] subStrings = filterString.split(" ");

		BooleanExpression clause = null;

		if (subStrings != null && subStrings.length > 0) {
			for (String str : subStrings) {
				clause = getGenderValue(doctor.gender, str);
				if (clause != null) {
					clauses.add(clause);
					continue;
				}

				clause = getIsDeleted(doctor.isDeleted, str);
				if (clause != null) {
					clauses.add(clause);
					continue;
				}

				clause = getMobile(doctor.mobile, str);
				if (clause != null) {
					clauses.add(clause);
					continue;
				}

				clause = getID(doctor.id, str);
				if (clause != null) {
					clauses.add(clause);
					continue;
				}

				clause = getChinesename(doctor.chineseName, str);
				if (clause != null) {
					clauses.add(clause);
					continue;
				}

			}
		}

		// Return
		if (clauses.size() == 1) {
			return clauses.get(0);
		} else if (clauses.size() > 1) {
			BooleanExpression firstClause = clauses.get(0);
			for (int i = 1; i < clauses.size(); i++) {
				firstClause.and(clauses.get(i));
			}
			return firstClause;
		}
		return null;
	}

	public static BooleanExpression getGenderValue(StringPath var, String str) {
		if ("男".equals(str)) {
			return var.eq("M");
		} else if ("女".equals(str)) {
			return var.eq("F");
		}
		return null;
	}

	public static BooleanExpression getIsDeleted(StringPath var, String str) {
		if ("已删除".equals(str)) {
			return var.eq("Y");
		}
		return null;
	}

	public static BooleanExpression getMobile(StringPath var, String str) {
		Pattern p = Pattern.compile("^[0-9]{8,13}$");
		Matcher m = p.matcher(str);

		if (m.find()) {
			return var.like(str);
		}
		return null;
	}

	public static BooleanExpression getID(NumberPath<Long> var, String str) {

		Pattern p = Pattern.compile("^[0-9]{1,7}$");
		Matcher m = p.matcher(str);
		if (m.find()) {
			Long value = EntityConvertUtil.getLong(str);
			return var.eq(value);
		}
		return null;
	}

	public static BooleanExpression getChinesename(StringPath var, String str) {
		Pattern p = Pattern.compile("^[\u4E00-\u9FA5A-Za-z]{1,8}$");
		Matcher m = p.matcher(str);

		if (m.find()) {
			return var.like(str);
		}
		return null;
	}

}
