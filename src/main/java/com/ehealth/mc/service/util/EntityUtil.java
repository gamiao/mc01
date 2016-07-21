package com.ehealth.mc.service.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.ex.ODataRuntimeException;
import org.apache.olingo.server.api.uri.UriParameter;

import com.ehealth.mc.bo.Doctor;
import com.ehealth.mc.bo.Patient;

public class EntityUtil {

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
			e.setId(createId(McEdmUtil.ES_DOCTORS_NAME, d.getId()));
			return e;
		}
		return null;
	}

	public static Entity getEntity(Patient d) {
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
			e.setId(createId(McEdmUtil.ES_PATIENTS_NAME, d.getId()));
			return e;
		}
		return null;
	}
	
	public static String getKeyText(List<UriParameter> keyParams, String keyName) {
		if (keyParams != null && keyParams.size() >0){
		    for (final UriParameter key : keyParams) {
		        // key
		        String kName = key.getName();
		        String kText = key.getText();
		        
		        if(kName.equals(keyName)){
		        	return kText;
		        }
		    }
		}
		return null;
	}
	

	public static URI createId(String entitySetName, Object id) {
		try {
			return new URI(entitySetName + "(" + String.valueOf(id) + ")");
		} catch (URISyntaxException e) {
			throw new ODataRuntimeException("Unable to create id for entity: "
					+ entitySetName, e);
		}
	}

}
