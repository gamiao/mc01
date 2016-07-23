package com.ehealth.mc.service.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.olingo.commons.api.Constants;
import org.apache.olingo.commons.api.data.ComplexValue;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Link;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.EdmNavigationProperty;
import org.apache.olingo.commons.api.ex.ODataRuntimeException;
import org.apache.olingo.server.api.uri.UriParameter;

import com.ehealth.mc.bo.Doctor;
import com.ehealth.mc.bo.OrderConversation;
import com.ehealth.mc.bo.OrderDetail;
import com.ehealth.mc.bo.OrderHeader;
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
			e.setId(createId(McEdmUtil.ES_ORDER_DETAILS_NAME, d.getId()));
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
			e.setId(createId(McEdmUtil.ES_ORDER_CONVS_NAME, d.getId()));
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
			e.setId(createId(McEdmUtil.ES_ORDERS_NAME, d.getId()));
			return e;
		}
		return null;
	}

	public static void setLink(final EdmNavigationProperty navigationProperty,
			final Entity srcEntity, final Entity targetEntity) {
		if (navigationProperty.isCollection()) {
			setLinks(srcEntity, navigationProperty.getName(), targetEntity);
		} else {
			setLink(srcEntity, navigationProperty.getName(), targetEntity);
		}
	}

	public static void setLink(final Entity entity,
			final String navigationPropertyName, final Entity target) {
		Link link = entity.getNavigationLink(navigationPropertyName);
		if (link == null) {
			link = new Link();
			link.setRel(Constants.NS_NAVIGATION_LINK_REL
					+ navigationPropertyName);
			link.setType(Constants.ENTITY_NAVIGATION_LINK_TYPE);
			link.setTitle(navigationPropertyName);
			link.setHref(target.getId().toASCIIString());

			entity.getNavigationLinks().add(link);
		}
		link.setInlineEntity(target);
	}

	public static void setLinks(final Entity entity,
			final String navigationPropertyName, final Entity... targets) {
		if (targets.length == 0) {
			return;
		}

		Link link = entity.getNavigationLink(navigationPropertyName);
		if (link == null) {
			link = new Link();
			link.setRel(Constants.NS_NAVIGATION_LINK_REL
					+ navigationPropertyName);
			link.setType(Constants.ENTITY_SET_NAVIGATION_LINK_TYPE);
			link.setTitle(navigationPropertyName);
			link.setHref(entity.getId().toASCIIString() + "/"
					+ navigationPropertyName);

			EntityCollection target = new EntityCollection();
			target.getEntities().addAll(Arrays.asList(targets));
			link.setInlineEntitySet(target);

			entity.getNavigationLinks().add(link);
		} else {
			link.getInlineEntitySet().getEntities()
					.addAll(Arrays.asList(targets));
		}
	}

	public static String getKeyText(List<UriParameter> keyParams, String keyName) {
		if (keyParams != null && keyParams.size() > 0) {
			for (final UriParameter key : keyParams) {
				// key
				String kName = key.getName();
				String kText = key.getText();

				if (kName.equals(keyName)) {
					return kText;
				}
			}
		}
		return null;
	}

	public static Integer getID(List<UriParameter> keyParams) {
		if (keyParams != null && keyParams.size() > 0) {
			String idText = getKeyText(keyParams, "ID");
			Integer idIntValue = Integer.valueOf(idText);
			return idIntValue;
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
