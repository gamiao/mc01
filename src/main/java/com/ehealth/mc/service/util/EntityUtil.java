package com.ehealth.mc.service.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.olingo.commons.api.Constants;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Link;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.edm.EdmBindingTarget;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmKeyPropertyRef;
import org.apache.olingo.commons.api.edm.EdmNavigationProperty;
import org.apache.olingo.commons.api.edm.EdmPrimitiveType;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeException;
import org.apache.olingo.commons.api.edm.EdmProperty;
import org.apache.olingo.commons.api.edm.EdmType;
import org.apache.olingo.commons.api.ex.ODataRuntimeException;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriInfoResource;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;
import org.apache.olingo.server.api.uri.UriResourceNavigation;

public class EntityUtil {
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

	public static Long getID(List<UriParameter> keyParams) {
		if (keyParams != null && keyParams.size() > 0) {
			String idText = getKeyText(keyParams, "ID");
			Long idIntValue = Long.valueOf(idText);
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

	public static boolean entityIdExists(int id, List<Entity> entityList) {

		for (Entity entity : entityList) {
			Integer existingID = (Integer) entity.getProperty("ID").getValue();
			if (existingID.intValue() == id) {
				return true;
			}
		}
		return false;
	}

	public static URI createId(Entity entity, String idPropertyName) {
		return createId(entity, idPropertyName, null);
	}

	private static URI createId(Entity entity, String idPropertyName,
			String navigationName) {
		try {
			StringBuilder sb = new StringBuilder(
					McEdmUtil.getEntitySetName(entity)).append("(");
			final Property property = entity.getProperty(idPropertyName);
			sb.append(property.asPrimitive()).append(")");
			if (navigationName != null) {
				sb.append("/").append(navigationName);
			}
			return new URI(sb.toString());
		} catch (URISyntaxException e) {
			throw new ODataRuntimeException(
					"Unable to create (Atom) id for entity: " + entity, e);
		}
	}

	public static EntityCollection getRelatedEntityCollection(Entity entity,
			UriResourceNavigation navigationResource) {
		return getRelatedEntityCollection(entity, navigationResource
				.getProperty().getName());
	}

	public static EntityCollection getRelatedEntityCollection(Entity entity,
			String navigationPropertyName) {
		final Link link = entity.getNavigationLink(navigationPropertyName);
		return link == null ? new EntityCollection() : link
				.getInlineEntitySet();
	}

	public static Entity getRelatedEntity(Entity entity,
			UriResourceNavigation navigationResource)
			throws ODataApplicationException {

		final EdmNavigationProperty edmNavigationProperty = navigationResource
				.getProperty();

		if (edmNavigationProperty.isCollection()) {
			return findEntity(edmNavigationProperty.getType(),
					EntityUtil.getRelatedEntityCollection(entity,
							navigationResource),
					navigationResource.getKeyPredicates());
		} else {
			final Link link = entity.getNavigationLink(edmNavigationProperty
					.getName());
			return link == null ? null : link.getInlineEntity();
		}
	}

	public static EdmEntitySet getEdmEntitySet(UriInfoResource uriInfo)
			throws ODataApplicationException {

		List<UriResource> resourcePaths = uriInfo.getUriResourceParts();
		// To get the entity set we have to interpret all URI segments
		if (!(resourcePaths.get(0) instanceof UriResourceEntitySet)) {
			// Here we should interpret the whole URI but in this example we do
			// not support navigation so we throw an
			// exception
			throw new ODataApplicationException(
					"Invalid resource type for first segment.",
					HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(),
					Locale.ENGLISH);
		}

		UriResourceEntitySet uriResource = (UriResourceEntitySet) resourcePaths
				.get(0);

		return uriResource.getEntitySet();
	}

	public static Entity findEntity(EdmEntityType edmEntityType,
			EntityCollection entitySet, List<UriParameter> keyParams) {

		List<Entity> entityList = entitySet.getEntities();

		// loop over all entities in order to find that one that matches
		// all keys in request e.g. contacts(ContactID=1, CompanyID=1)
		for (Entity entity : entityList) {
			boolean foundEntity = entityMatchesAllKeys(edmEntityType, entity,
					keyParams);
			if (foundEntity) {
				return entity;
			}
		}

		return null;
	}

	public static boolean entityMatchesAllKeys(EdmEntityType edmEntityType,
			Entity rt_entity, List<UriParameter> keyParams) {

		// loop over all keys
		for (final UriParameter key : keyParams) {
			// key
			String keyName = key.getName();
			String keyText = key.getText();

			// note: below line doesn't consider: keyProp can be part of a
			// complexType in V4
			// in such case, it would be required to access it via
			// getKeyPropertyRef()
			// but since this isn't the case in our model, we ignore it in our
			// implementation
			EdmProperty edmKeyProperty = (EdmProperty) edmEntityType
					.getProperty(keyName);
			// Edm: we need this info for the comparison below
			Boolean isNullable = edmKeyProperty.isNullable();
			Integer maxLength = edmKeyProperty.getMaxLength();
			Integer precision = edmKeyProperty.getPrecision();
			Boolean isUnicode = edmKeyProperty.isUnicode();
			Integer scale = edmKeyProperty.getScale();
			// get the EdmType in order to compare
			EdmType edmType = edmKeyProperty.getType();
			// if(EdmType instanceof EdmPrimitiveType) // do we need this?
			EdmPrimitiveType edmPrimitiveType = (EdmPrimitiveType) edmType;

			// Runtime data: the value of the current entity
			// don't need to check for null, this is done in FWK
			Object valueObject = rt_entity.getProperty(keyName).getValue();
			// TODO if the property is a complex type

			// now need to compare the valueObject with the keyText String
			// this is done using the type.valueToString
			String valueAsString = null;
			try {
				valueAsString = edmPrimitiveType.valueToString(valueObject,
						isNullable, maxLength, precision, scale, isUnicode);
			} catch (EdmPrimitiveTypeException e) {
				return false; // TODO proper Exception handling
			}

			if (valueAsString == null) {
				return false;
			}

			boolean matches = valueAsString.equals(keyText);
			// if any of the key properties is not found in the entity, we don't
			// need to search further
			if (!matches) {
				return false;
			}
			// if the given key value is found in the current entity, continue
			// with the next key
		}

		return true;
	}

	/**
	 * Example: For the following navigation:
	 * DemoService.svc/Categories(1)/Products we need the EdmEntitySet for the
	 * navigation property "Products"
	 *
	 * This is defined as follows in the metadata: <code>
	 * 
	 * <EntitySet Name="Categories" EntityType="OData.Demo.Category">
	 * <NavigationPropertyBinding Path="Products" Target="Products"/>
	 * </EntitySet>
	 * </code> The "Target" attribute specifies the target EntitySet Therefore
	 * we need the startEntitySet "Categories" in order to retrieve the target
	 * EntitySet "Products"
	 */
	public static EdmEntitySet getNavigationTargetEntitySet(
			EdmEntitySet startEdmEntitySet,
			EdmNavigationProperty edmNavigationProperty)
			throws ODataApplicationException {

		EdmEntitySet navigationTargetEntitySet = null;

		String navPropName = edmNavigationProperty.getName();
		EdmBindingTarget edmBindingTarget = startEdmEntitySet
				.getRelatedBindingTarget(navPropName);
		if (edmBindingTarget == null) {
			throw new ODataApplicationException("Not supported.",
					HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ROOT);
		}

		if (edmBindingTarget instanceof EdmEntitySet) {
			navigationTargetEntitySet = (EdmEntitySet) edmBindingTarget;
		} else {
			throw new ODataApplicationException("Not supported.",
					HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ROOT);
		}

		return navigationTargetEntitySet;
	}

	public static void createLink(
			final EdmNavigationProperty navigationProperty,
			final Entity srcEntity, final Entity destEntity) {
		setLink(navigationProperty, srcEntity, destEntity);

		final EdmNavigationProperty partnerNavigationProperty = navigationProperty
				.getPartner();
		if (partnerNavigationProperty != null) {
			setLink(partnerNavigationProperty, destEntity, srcEntity);
		}
	}

	public static boolean isKey(EdmEntityType edmEntityType, String propertyName) {
		List<EdmKeyPropertyRef> keyPropertyRefs = edmEntityType
				.getKeyPropertyRefs();
		for (EdmKeyPropertyRef propRef : keyPropertyRefs) {
			String keyPropertyName = propRef.getName();
			if (keyPropertyName.equals(propertyName)) {
				return true;
			}
		}
		return false;
	}

}
