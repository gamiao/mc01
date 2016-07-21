package com.ehealth.mc.service.impl;

import java.util.List;
import java.util.Locale;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Link;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmNavigationProperty;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResourceNavigation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehealth.mc.odata.util.Util;
import com.ehealth.mc.service.DoctorService;
import com.ehealth.mc.service.OrderService;
import com.ehealth.mc.service.OverallService;
import com.ehealth.mc.service.PatientService;
import com.ehealth.mc.service.util.EntityUtil;
import com.ehealth.mc.service.util.McEdmUtil;

@Service("overallService")
public class OverallServiceImpl implements OverallService {

	@Autowired
	DoctorService dcotorService;

	@Autowired
	PatientService patientService;

	@Autowired
	OrderService orderService;

	@Override
	public EntityCollection findAll(EdmEntitySet edmEntitySet) {
		EntityCollection entityCollection = new EntityCollection();
		if (McEdmUtil.ES_DOCTORS_NAME.equals(edmEntitySet.getName())) {
			List<Entity> entityList = entityCollection.getEntities();
			List<Entity> queryResult = dcotorService.findAll();
			entityList.addAll(queryResult);
			return entityCollection;
		} else if (McEdmUtil.ES_ORDERS_NAME.equals(edmEntitySet.getName())) {
			List<Entity> entityList = entityCollection.getEntities();
			List<Entity> queryResult = orderService.findAll();
			entityList.addAll(queryResult);
			return entityCollection;
		}
		return null;
	}

	private Entity getEntity(EdmEntityType edmEntityType,
			List<UriParameter> keyParams, List<Entity> entityList)
			throws ODataApplicationException {

		// the list of entities at runtime
		EntityCollection entitySet = getEntityCollection(entityList);

		/* generic approach to find the requested entity */
		Entity requestedEntity = Util.findEntity(edmEntityType, entitySet,
				keyParams);

		if (requestedEntity == null) {
			// this variable is null if our data doesn't contain an entity for
			// the requested key
			// Throw suitable exception
			throw new ODataApplicationException(
					"Entity for requested key doesn't exist",
					HttpStatusCode.NOT_FOUND.getStatusCode(), Locale.ENGLISH);
		}

		return requestedEntity;
	}

	private EntityCollection getEntityCollection(final List<Entity> entityList) {

		EntityCollection retEntitySet = new EntityCollection();
		retEntitySet.getEntities().addAll(entityList);

		return retEntitySet;
	}

	@Override
	public Entity readEntityData(EdmEntitySet edmEntitySet,
			List<UriParameter> keyParams) throws ODataApplicationException {
		EdmEntityType edmEntityType = edmEntitySet.getEntityType();
		if (edmEntitySet.getName().equals(McEdmUtil.ES_DOCTORS_NAME)) {
			return getEntity(edmEntityType, keyParams, dcotorService.findAll());
		} else if (edmEntitySet.getName().equals(McEdmUtil.ES_PATIENTS_NAME)) {
			String idValue = EntityUtil.getKeyText(keyParams, "ID");
			return patientService.findById(Integer.valueOf(idValue));
		}
		return null;
	}

	@Override
	public Entity getRelatedEntity(Entity entity,
			UriResourceNavigation navigationResource)
			throws ODataApplicationException {

		final EdmNavigationProperty edmNavigationProperty = navigationResource
				.getProperty();

		if (edmNavigationProperty.isCollection()) {
			return Util.findEntity(edmNavigationProperty.getType(),
					getRelatedEntityCollection(entity, navigationResource),
					navigationResource.getKeyPredicates());
		} else {
			final Link link = entity.getNavigationLink(edmNavigationProperty
					.getName());
			return link == null ? null : link.getInlineEntity();
		}
	}

	public EntityCollection getRelatedEntityCollection(Entity entity,
			UriResourceNavigation navigationResource) {
		return getRelatedEntityCollection(entity, navigationResource
				.getProperty().getName());
	}

	public EntityCollection getRelatedEntityCollection(Entity entity,
			String navigationPropertyName) {
		final Link link = entity.getNavigationLink(navigationPropertyName);
		return link == null ? new EntityCollection() : link
				.getInlineEntitySet();
	}

}
