package com.ehealth.mc.service.util;

import java.net.URI;

import org.apache.olingo.commons.api.Constants;
import org.apache.olingo.commons.api.data.ComplexValue;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Link;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;

import com.ehealth.mc.bo.Doctor;
import com.ehealth.mc.bo.OrderConversation;
import com.ehealth.mc.bo.OrderDetail;
import com.ehealth.mc.bo.OrderHeader;
import com.ehealth.mc.bo.Patient;

public class EntityConvertUtil {

	public static Doctor getDoctor(Entity e) {
		if (e != null) {
			Doctor d = new Doctor();
			if (isValidId(getPropertyStringValue(e, "ID"))) {
				d.setId(Integer.valueOf((getPropertyStringValue(e, "ID"))));
			}
			d.setAddress(getPropertyStringValue(e, "Address"));
			d.setAvatar(getPropertyStringValue(e, "Avatar"));
			d.setBirthday(getPropertyStringValue(e, "Birthday"));
			d.setChineseName(getPropertyStringValue(e, "Name"));
			// d.setGender(getPropertyStringValue("Gender"));
			d.setMedicalLevel(getPropertyStringValue(e, "MedicalLevel"));
			d.setMobile(getPropertyStringValue(e, "Mobile"));
			return d;
		}
		return null;

	}

	public static Patient getPatient(Entity e) {
		if (e != null) {
			Patient d = new Patient();
			if (isValidId(getPropertyStringValue(e, "ID"))) {
				d.setId(Integer.valueOf((getPropertyStringValue(e, "ID"))));
			}
			d.setAddress(getPropertyStringValue(e, "Address"));
			d.setAvatar(getPropertyStringValue(e, "Avatar"));
			d.setBirthday(getPropertyStringValue(e, "Birthday"));
			d.setChineseName(getPropertyStringValue(e, "Name"));
			// d.setGender(getPropertyStringValue("Gender"));
			d.setMobile(getPropertyStringValue(e, "Mobile"));
			return d;
		}
		return null;

	}

	private static boolean isValidId(String string) {
		return false;
	}

	private static String getPropertyStringValue(Entity e, String propName) {
		if (e != null && e.getProperty(propName) != null) {
			Property p = e.getProperty(propName);
			if (p.getValue() != null) {
				return p.getValue().toString();
			}
		}

		return null;
	}

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
			e.setId(EntityUtil.createId(McEdmUtil.ES_DOCTORS_NAME, d.getId()));
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
			e.setId(EntityUtil.createId(McEdmUtil.ES_PATIENTS_NAME, d.getId()));
			return e;
		}
		return null;
	}

	public static Entity getEntity(OrderDetail d) {
		if (d != null) {
			Entity e = new Entity();
			e.addProperty(new Property(null, "ID", ValueType.PRIMITIVE, d
					.getId()));
			e.addProperty(new Property(null, "Type", ValueType.PRIMITIVE, d
					.getType()));
			e.addProperty(new Property(null, "Title", ValueType.PRIMITIVE, d
					.getTitle()));
			e.addProperty(new Property(null, "Description",
					ValueType.PRIMITIVE, d.getDescription()));
			e.addProperty(new Property(null, "Pics", ValueType.PRIMITIVE, d
					.getPictures()));
			e.setId(EntityUtil.createId(McEdmUtil.ES_ORDER_DETAILS_NAME,
					d.getId()));
			return e;
		}
		return null;
	}

	public static Entity getEntity(OrderConversation d) {
		if (d != null) {
			Entity e = new Entity();
			e.addProperty(new Property(null, "ID", ValueType.PRIMITIVE, d
					.getId()));
			e.addProperty(new Property(null, "Type", ValueType.PRIMITIVE, d
					.getType()));
			e.addProperty(new Property(null, "Title", ValueType.PRIMITIVE, d
					.getTitle()));
			e.addProperty(new Property(null, "Description",
					ValueType.PRIMITIVE, d.getDescription()));
			e.addProperty(new Property(null, "Pics", ValueType.PRIMITIVE, d
					.getPictures()));
			e.setId(EntityUtil.createId(McEdmUtil.ES_ORDER_CONVS_NAME,
					d.getId()));
			return e;
		}
		return null;
	}

	public static Entity getEntity(OrderHeader d) {
		if (d != null) {
			Entity e = new Entity();
			e.addProperty(new Property(null, "ID", ValueType.PRIMITIVE, d
					.getId()));
			e.addProperty(new Property(null, "Status", ValueType.PRIMITIVE, d
					.getStatus()));
			e.addProperty(new Property(null, "IsArchived", ValueType.PRIMITIVE,
					d.getIsArchived()));
			e.addProperty(new Property(null, "IsEnabled", ValueType.PRIMITIVE,
					d.getIsEnabled()));
			e.addProperty(new Property(null, "IsDeleted", ValueType.PRIMITIVE,
					d.getIsDeleted()));
			e.addProperty(new Property(null, "Patient", ValueType.COMPLEX,
					getComplexValue(getEntity(d.getPatient()))));
			e.addProperty(new Property(null, "Doctor", ValueType.COMPLEX,
					getComplexValue(getEntity(d.getDoctor()))));
			e.addProperty(new Property(null, "Detail", ValueType.COMPLEX,
					getComplexValue(getEntity(d.getOrderDetail()))));

			EntityCollection convs = new EntityCollection();
			if (d.getOrderConversations() != null) {
				for (OrderConversation i : d.getOrderConversations()) {
					if (i != null) {
						Entity conv = getEntity(i);
						if (conv != null) {
							convs.getEntities().add(conv);
						}
					}
				}
			}
			try {
				convs.setId(new URI("OrderConvs"));

			} catch (Exception ex) {

			}
			String navigationPropertyName = "OrderConvs";

			Link link = e.getNavigationLink(navigationPropertyName);
			if (link == null) {
				link = new Link();
				link.setRel(Constants.NS_NAVIGATION_LINK_REL
						+ navigationPropertyName);
				link.setType(Constants.ENTITY_NAVIGATION_LINK_TYPE);
				link.setTitle(navigationPropertyName);
				link.setHref(convs.getId().toASCIIString());

				e.getNavigationLinks().add(link);
			}
			link.setInlineEntitySet(convs);

			e.addProperty(new Property(null, "OrderConvs",
					ValueType.COLLECTION_ENTITY, convs));
			e.setId(EntityUtil.createId(McEdmUtil.ES_ORDERS_NAME, d.getId()));
			return e;
		}
		return null;
	}

	public static ComplexValue getComplexValue(Entity e) {
		if (e != null) {
			ComplexValue cv = new ComplexValue();
			cv.getValue().addAll(e.getProperties());
			cv.setId(e.getId());
			return cv;
		}
		return null;
	}

}
