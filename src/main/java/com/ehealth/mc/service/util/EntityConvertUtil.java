package com.ehealth.mc.service.util;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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

	public static String PATIENT_DEFAULT_AVATAR = "patient_default01.png";

	public static String DOCTOR_DEFAULT_AVATAR = "doctor_default01.png";

	public static Doctor getDoctor(Entity e, Doctor original) {
		if (e != null) {
			Doctor obj = new Doctor();
			if (original != null) {
				obj = original;
			} else {
				obj.setCreateTime(new Date());
				obj.setIsDeleted("Y");
				obj.setAvatar(DOCTOR_DEFAULT_AVATAR);
				obj.setLogin(getPropertyStringValue(e, "Login"));
				obj.setPassword(getPropertyStringValue(e, "Password"));
			}
			obj.setAddress(getPropertyStringValue(e, "Address"));
			obj.setMail(getPropertyStringValue(e, "Mail"));
			obj.setBirthday(getPropertyDateValue(e, "Birthday"));
			obj.setChineseName(getPropertyStringValue(e, "Name"));
			obj.setGender(getPropertyStringValue(e, "Gender"));
			obj.setMedicalLevel(getPropertyStringValue(e, "MedicalLevel"));
			obj.setPrice(getPropertyIntegerValue(e, "Price"));
			obj.setMobile(getPropertyStringValue(e, "Mobile"));
			return obj;
		}
		return null;
	}

	public static Patient getPatient(Entity e, Patient original) {
		if (e != null) {
			Patient obj = new Patient();
			if (original != null) {
				obj = original;
			} else {
				obj.setCreateTime(new Date());
				obj.setIsDeleted("N");
				obj.setAvatar(PATIENT_DEFAULT_AVATAR);
				obj.setLogin(getPropertyStringValue(e, "Login"));
				obj.setPassword(getPropertyStringValue(e, "Password"));
			}
			obj.setAddress(getPropertyStringValue(e, "Address"));
			obj.setMail(getPropertyStringValue(e, "Mail"));
			obj.setBirthday(getPropertyDateValue(e, "Birthday"));
			obj.setChineseName(getPropertyStringValue(e, "Name"));
			obj.setGender(getPropertyStringValue(e, "Gender"));
			obj.setMobile(getPropertyStringValue(e, "Mobile"));
			return obj;
		}
		return null;
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

	private static Date getPropertyDateValue(Entity e, String propName) {
		if (e != null && e.getProperty(propName) != null) {
			Property p = e.getProperty(propName);
			if (p.getValue() != null && p.getValue() instanceof java.util.GregorianCalendar) {
				GregorianCalendar timeValue = (GregorianCalendar) p.getValue();
				return timeValue.getTime();
			}
		}
		return null;
	}

	private static Long getPropertyLongValue(Entity e, String propName) {
		if (e != null && e.getProperty(propName) != null) {
			Property p = e.getProperty(propName);
			if (p.getValue() != null) {
				Long value = getLong(p.getValue().toString());
				if (value != null) {
					return value;
				}
			}
		}
		return null;
	}

	private static Integer getPropertyIntegerValue(Entity e, String propName) {
		if (e != null && e.getProperty(propName) != null) {
			Property p = e.getProperty(propName);
			if (p.getValue() != null) {
				Integer value = getInteger(p.getValue().toString());
				if (value != null) {
					return value;
				}
			}
		}
		return null;
	}

	public static Entity getEntity(Doctor d) {
		if (d != null) {
			Entity e = new Entity();
			e.addProperty(new Property(null, "ID", ValueType.PRIMITIVE, d.getId()));
			e.addProperty(new Property(null, "Login", ValueType.PRIMITIVE, d.getLogin()));
			e.addProperty(new Property(null, "Name", ValueType.PRIMITIVE, d.getChineseName()));
			e.addProperty(new Property(null, "Gender", ValueType.PRIMITIVE, d.getGender()));
			e.addProperty(new Property(null, "Avatar", ValueType.PRIMITIVE, d.getAvatar()));
			e.addProperty(new Property(null, "Address", ValueType.PRIMITIVE, d.getAddress()));
			e.addProperty(new Property(null, "IsDeleted", ValueType.PRIMITIVE, d.getIsDeleted()));
			e.addProperty(new Property(null, "Mobile", ValueType.PRIMITIVE, d.getMobile()));
			e.addProperty(new Property(null, "Mail", ValueType.PRIMITIVE, d.getMail()));
			e.addProperty(new Property(null, "Birthday", ValueType.PRIMITIVE, d.getBirthday()));
			e.addProperty(new Property(null, "CreateTime", ValueType.PRIMITIVE,
					FormatUtil.getUIShowingTime(d.getCreateTime())));
			e.addProperty(new Property(null, "MedicalLevel", ValueType.PRIMITIVE, d.getMedicalLevel()));
			e.addProperty(new Property(null, "Price", ValueType.PRIMITIVE, d.getPrice()));
			e.setId(EntityUtil.createId(McEdmUtil.ES_DOCTORS_NAME, d.getId()));
			return e;
		}
		return null;
	}

	public static Entity getEntity(Patient d) {
		if (d != null) {
			Entity e = new Entity();
			e.addProperty(new Property(null, "ID", ValueType.PRIMITIVE, d.getId()));
			e.addProperty(new Property(null, "Login", ValueType.PRIMITIVE, d.getLogin()));
			e.addProperty(new Property(null, "Name", ValueType.PRIMITIVE, d.getChineseName()));
			e.addProperty(new Property(null, "Gender", ValueType.PRIMITIVE, d.getGender()));
			e.addProperty(new Property(null, "Avatar", ValueType.PRIMITIVE, d.getAvatar()));
			e.addProperty(new Property(null, "Address", ValueType.PRIMITIVE, d.getAddress()));
			e.addProperty(new Property(null, "IsDeleted", ValueType.PRIMITIVE, d.getIsDeleted()));
			e.addProperty(new Property(null, "Mobile", ValueType.PRIMITIVE, d.getMobile()));
			e.addProperty(new Property(null, "Mail", ValueType.PRIMITIVE, d.getMail()));
			e.addProperty(new Property(null, "Birthday", ValueType.PRIMITIVE, d.getBirthday()));
			e.addProperty(new Property(null, "CreateTime", ValueType.PRIMITIVE,
					FormatUtil.getUIShowingTime(d.getCreateTime())));
			e.setId(EntityUtil.createId(McEdmUtil.ES_PATIENTS_NAME, d.getId()));
			return e;
		}
		return null;
	}

	public static Entity getEntity(OrderDetail d) {
		if (d != null) {
			Entity e = new Entity();
			e.addProperty(new Property(null, "ID", ValueType.PRIMITIVE, d.getId()));
			e.addProperty(new Property(null, "Type", ValueType.PRIMITIVE, d.getType()));
			e.addProperty(new Property(null, "Title", ValueType.PRIMITIVE, d.getTitle()));
			e.addProperty(new Property(null, "IssueAge", ValueType.PRIMITIVE, d.getIssueAge()));
			e.addProperty(new Property(null, "Description", ValueType.PRIMITIVE, d.getDescription()));
			e.addProperty(new Property(null, "Description2", ValueType.PRIMITIVE, d.getDescription2()));
			e.addProperty(new Property(null, "Description3", ValueType.PRIMITIVE, d.getDescription3()));
			e.addProperty(new Property(null, "Description4", ValueType.PRIMITIVE, d.getDescription4()));
			e.addProperty(new Property(null, "Description5", ValueType.PRIMITIVE, d.getDescription5()));
			e.addProperty(new Property(null, "Pics", ValueType.PRIMITIVE, d.getPictures()));
			e.addProperty(new Property(null, "CreateTime", ValueType.PRIMITIVE,
					FormatUtil.getUIShowingTime(d.getCreateTime())));
			e.setId(EntityUtil.createId(McEdmUtil.ES_ORDER_DETAILS_NAME, d.getId()));
			return e;
		}
		return null;
	}

	public static List<Entity> getOrderEntityList(List<OrderHeader> objList) {
		if (objList != null && objList.size() > 0) {
			List<Entity> entityList = new ArrayList<Entity>();
			for (OrderHeader obj : objList) {
				Entity entity = getOrderEntityWithoutOrderConvs(obj);
				if (entity != null) {
					entityList.add(entity);
				}
			}
			return entityList;
		}

		return null;
	}

	public static List<Entity> getPatientEntityList(List<Patient> objList) {
		if (objList != null && objList.size() > 0) {
			List<Entity> entityList = new ArrayList<Entity>();
			for (Patient obj : objList) {
				Entity entity = getEntity(obj);
				if (entity != null) {
					entityList.add(entity);
				}
			}
			return entityList;
		}
		return null;
	}

	public static List<Entity> getDoctorEntityList(List<Doctor> objList) {
		if (objList != null && objList.size() > 0) {
			List<Entity> entityList = new ArrayList<Entity>();
			for (Doctor obj : objList) {
				Entity entity = getEntity(obj);
				if (entity != null) {
					entityList.add(entity);
				}
			}
			return entityList;
		}
		return null;
	}

	public static Entity getEntity(OrderConversation d) {
		if (d != null) {
			Entity e = new Entity();
			e.addProperty(new Property(null, "ID", ValueType.PRIMITIVE, d.getId()));
			e.addProperty(new Property(null, "Type", ValueType.PRIMITIVE, d.getType()));
			e.addProperty(new Property(null, "Owner", ValueType.PRIMITIVE, d.getOwner()));
			e.addProperty(new Property(null, "Title", ValueType.PRIMITIVE, d.getTitle()));
			e.addProperty(new Property(null, "Description", ValueType.PRIMITIVE, d.getDescription()));
			e.addProperty(new Property(null, "Pics", ValueType.PRIMITIVE, d.getPictures()));
			e.addProperty(new Property(null, "CreateTime", ValueType.PRIMITIVE,
					FormatUtil.getUIShowingTime(d.getCreateTime())));
			e.setId(EntityUtil.createId(McEdmUtil.ES_ORDER_CONVS_NAME, d.getId()));
			return e;
		}
		return null;
	}

	public static OrderConversation getOrderConversation(Entity e) {
		if (e != null) {
			OrderConversation obj = new OrderConversation();
			obj.setCreateTime(new Date());
			obj.setDescription(getPropertyStringValue(e, "Description"));
			obj.setType(getPropertyStringValue(e, "Type"));
			obj.setTitle(getPropertyStringValue(e, "Title"));
			obj.setOwner(getPropertyStringValue(e, "Owner"));
			return obj;
		}
		return null;
	}

	public static Entity getOrderConvEntityByOrderDetail(OrderDetail d) {
		if (d != null) {
			Entity e = new Entity();
			e.addProperty(new Property(null, "ID", ValueType.PRIMITIVE, d.getId()));
			e.addProperty(new Property(null, "Owner", ValueType.PRIMITIVE, "P"));
			e.addProperty(new Property(null, "Type", ValueType.PRIMITIVE, "TEXT"));
			e.addProperty(new Property(null, "Title", ValueType.PRIMITIVE, d.getTitle()));
			e.addProperty(new Property(null, "IssueAge", ValueType.PRIMITIVE, d.getIssueAge()));
			e.addProperty(new Property(null, "Description", ValueType.PRIMITIVE, d.getDescription()));
			e.addProperty(new Property(null, "Description2", ValueType.PRIMITIVE, d.getDescription2()));
			e.addProperty(new Property(null, "Description3", ValueType.PRIMITIVE, d.getDescription3()));
			e.addProperty(new Property(null, "Description4", ValueType.PRIMITIVE, d.getDescription4()));
			e.addProperty(new Property(null, "Description5", ValueType.PRIMITIVE, d.getDescription5()));
			e.addProperty(new Property(null, "Pics", ValueType.PRIMITIVE, d.getPictures()));
			e.addProperty(new Property(null, "CreateTime", ValueType.PRIMITIVE,
					FormatUtil.getUIShowingTime(d.getCreateTime())));
			e.setId(EntityUtil.createId(McEdmUtil.ES_ORDER_CONVS_NAME, 0));
			return e;
		}
		return null;
	}

	public static Entity getOrderEntityWithoutOrderConvs(OrderHeader d) {
		if (d != null) {
			Entity e = new Entity();
			e.addProperty(new Property(null, "ID", ValueType.PRIMITIVE, d.getId()));
			e.addProperty(new Property(null, "Status", ValueType.PRIMITIVE, d.getStatus()));
			e.addProperty(new Property(null, "IsArchived", ValueType.PRIMITIVE, d.getIsArchived()));
			e.addProperty(new Property(null, "IsEnabled", ValueType.PRIMITIVE, d.getIsEnabled()));
			e.addProperty(new Property(null, "IsDeleted", ValueType.PRIMITIVE, d.getIsDeleted()));
			e.addProperty(
					new Property(null, "CTPatient", ValueType.COMPLEX, getComplexValue(getEntity(d.getPatient()))));
			e.addProperty(new Property(null, "CTDoctor", ValueType.COMPLEX, getComplexValue(getEntity(d.getDoctor()))));
			e.addProperty(
					new Property(null, "CTDetail", ValueType.COMPLEX, getComplexValue(getEntity(d.getOrderDetail()))));
			e.addProperty(new Property(null, "CreateTime", ValueType.PRIMITIVE,
					FormatUtil.getUIShowingTime(d.getCreateTime())));
			e.setId(EntityUtil.createId(McEdmUtil.ES_ORDERS_NAME, d.getId()));
			return e;
		}
		return null;
	}

	public static Entity getEntity(OrderHeader d) {
		if (d != null) {
			Entity e = new Entity();
			e.addProperty(new Property(null, "ID", ValueType.PRIMITIVE, d.getId()));
			e.addProperty(new Property(null, "Status", ValueType.PRIMITIVE, d.getStatus()));
			e.addProperty(new Property(null, "IsArchived", ValueType.PRIMITIVE, d.getIsArchived()));
			e.addProperty(new Property(null, "IsEnabled", ValueType.PRIMITIVE, d.getIsEnabled()));
			e.addProperty(new Property(null, "IsDeleted", ValueType.PRIMITIVE, d.getIsDeleted()));
			e.addProperty(
					new Property(null, "CTPatient", ValueType.COMPLEX, getComplexValue(getEntity(d.getPatient()))));
			e.addProperty(new Property(null, "CTDoctor", ValueType.COMPLEX, getComplexValue(getEntity(d.getDoctor()))));
			e.addProperty(
					new Property(null, "CTDetail", ValueType.COMPLEX, getComplexValue(getEntity(d.getOrderDetail()))));
			e.addProperty(new Property(null, "CreateTime", ValueType.PRIMITIVE,
					FormatUtil.getUIShowingTime(d.getCreateTime())));

			EntityCollection convs = new EntityCollection();
			if (d.getOrderDetail() != null) {
				Entity firstConv = getOrderConvEntityByOrderDetail(d.getOrderDetail());
				if (firstConv != null) {
					convs.getEntities().add(firstConv);
				}
			}
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
				link.setRel(Constants.NS_NAVIGATION_LINK_REL + navigationPropertyName);
				link.setType(Constants.ENTITY_NAVIGATION_LINK_TYPE);
				link.setTitle(navigationPropertyName);
				link.setHref(convs.getId().toASCIIString());

				e.getNavigationLinks().add(link);
			}
			link.setInlineEntitySet(convs);

			e.addProperty(new Property(null, "OrderConvs", ValueType.COLLECTION_ENTITY, convs));
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

	public static OrderDetail getOrderDetail(ComplexValue v) {
		if (v != null) {
			OrderDetail d = new OrderDetail();
			if (v != null && v.getValue() != null && v.getValue().size() > 0) {
				for (Property p : v.getValue()) {
					if ("ID".equals(p.getName())) {
						d.setId(getLong(p.getValue().toString()));
					} else if ("Type".equals(p.getName())) {
						d.setType((p.getValue().toString()));
					} else if ("Title".equals(p.getName())) {
						d.setTitle((p.getValue().toString()));
					} else if ("IssueAge".equals(p.getName())) {
						d.setIssueAge((new Integer(p.getValue().toString())));
					} else if ("Description".equals(p.getName())) {
						d.setDescription((p.getValue().toString()));
					} else if ("Description2".equals(p.getName())) {
						d.setDescription2((p.getValue().toString()));
					} else if ("Description3".equals(p.getName())) {
						d.setDescription3((p.getValue().toString()));
					} else if ("Description4".equals(p.getName())) {
						d.setDescription4((p.getValue().toString()));
					} else if ("Description5".equals(p.getName())) {
						d.setDescription5((p.getValue().toString()));
					} else if ("Pics".equals(p.getName())) {
						d.setPictures((p.getValue().toString()));
					}
				}
				if (d.getId() == null) {
					d.setCreateTime(new Date());
				}
			}
			return d;
		}
		return null;
	}

	public static OrderHeader getOrderHeader(Entity e) {
		if (e != null) {
			OrderHeader d = new OrderHeader();
			Long id = getID(e);
			d.setId(id);
			if (id == null) {
				d.setCreateTime(new Date());
				d.setStatus("new");
				d.setIsArchived("N");
				d.setIsEnabled("Y");
				d.setIsDeleted("N");
			} else {
				d.setStatus(getPropertyStringValue(e, "Status"));
				d.setIsArchived(getPropertyStringValue(e, "IsArchived"));
				d.setIsEnabled(getPropertyStringValue(e, "IsEnabled"));
				d.setIsDeleted(getPropertyStringValue(e, "IsDeleted"));
			}
			return d;
		}
		return null;
	}

	public static ComplexValue getOrderDetailComplexValue(Entity e) {
		Property p = e.getProperty("CTDetail");
		if (p != null && p.getType().endsWith(McEdmUtil.CT_ORDER_DETAIL_NAME)) {
			return (ComplexValue) p.getValue();
		}
		return null;
	}

	public static Long getPatientIDFromOrderEntity(Entity e) {
		Property p = e.getProperty("CTPatient");
		if (p != null) {
			ComplexValue cv = (ComplexValue) p.getValue();
			return getID(cv);
		}
		return null;
	}

	public static Long getDoctorIDFromOrderEntity(Entity e) {
		Property p = e.getProperty("CTDoctor");
		if (p != null) {
			ComplexValue cv = (ComplexValue) p.getValue();
			return getID(cv);
		}
		return null;
	}

	public static Long getOrderDetailIDFromOrderEntity(Entity e) {
		Property p = e.getProperty("CTDetail");
		if (p != null) {
			ComplexValue cv = (ComplexValue) p.getValue();
			return getID(cv);
		}
		return null;
	}

	private static Long getID(ComplexValue v) {
		if (v != null && v.getValue() != null && v.getValue().size() > 0) {
			for (Property p : v.getValue()) {
				if ("ID".equals(p.getName())) {
					return getLong(p.getValue().toString());
				}
			}
		}
		return null;
	}

	public static Long getID(Entity e) {
		return getLong((getPropertyStringValue(e, "ID")));
	}

	public static Long getLong(String str) {
		if (str != null) {
			try {
				Long idValue = Long.valueOf(str);
				return idValue;
			} catch (Exception exp) {
				return null;
			}

		}
		return null;

	}

	public static Integer getInteger(String str) {
		if (str != null) {
			try {
				Integer value = Integer.valueOf(str);
				return value;
			} catch (Exception exp) {
				return null;
			}

		}
		return null;

	}

	public static void updateOrderHeader(OrderHeader orderHeader, Entity e) {
		if (e != null) {
			String status = getPropertyStringValue(e, "Status");
			String isArchived = getPropertyStringValue(e, "IsArchived");
			String isEnabled = getPropertyStringValue(e, "IsEnabled");
			String isDeleted = getPropertyStringValue(e, "IsDeleted");

			if (status != null && !status.equals("")) {
				orderHeader.setStatus(status);
			}
			if (checkBooleanValue(isArchived)) {
				orderHeader.setIsArchived(isArchived);
			}
			if (checkBooleanValue(isEnabled)) {
				orderHeader.setIsEnabled(isEnabled);
			}
			if (checkBooleanValue(isDeleted)) {
				orderHeader.setIsDeleted(isDeleted);
			}
		}

	}

	public static boolean checkBooleanValue(String str) {
		if (str != null && !str.equals("")) {
			if (str.endsWith("Y") || str.equals("N")) {
				return true;
			}
		}
		return false;
	}

}
