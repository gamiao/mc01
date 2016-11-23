package com.ehealth.mc.common;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class JsonAnnotationExclusionStrategy implements ExclusionStrategy {

	@Override
	public boolean shouldSkipClass(Class<?> clazz) {
		return clazz.getAnnotation(JsonExclude.class) != null;
	}

	@Override
	public boolean shouldSkipField(FieldAttributes f) {
		return f.getName().equals("orderConversations");
	}
}