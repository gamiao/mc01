package com.ehealth.mc.service.util;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;

import com.ehealth.mc.bo.Doctor;

public class EntityConvertUtil {

	public static Entity getEntity(Doctor d) {
		if (d != null) {

			Entity e = new Entity();
			e.addProperty(new Property(null, "ID", ValueType.PRIMITIVE, d
					.getId()));
			e.addProperty(new Property(null, "Name", ValueType.PRIMITIVE, d
					.getChineseName()));
			e.addProperty(new Property(null, "Gender", ValueType.PRIMITIVE, d
					.getGender()));
			e.addProperty(new Property(null, "Avatar", ValueType.PRIMITIVE, d
					.getAvatar()));
			e.addProperty(new Property(null, "Address", ValueType.PRIMITIVE, d
					.getAddress()));
			e.addProperty(new Property(null, "Mobile", ValueType.PRIMITIVE, d
					.getMobile()));
			e.addProperty(new Property(null, "Birthday", ValueType.PRIMITIVE, d
					.getBirthday()));
			e.addProperty(new Property(null, "MedicalLevel",
					ValueType.PRIMITIVE, d.getMedicalLevel()));

			return e;
		}
		return null;
	}

}
