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
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.ODataRequest;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.deserializer.DeserializerException;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehealth.mc.service.DoctorService;
import com.ehealth.mc.service.OrderService;
import com.ehealth.mc.service.OverallService;
import com.ehealth.mc.service.PatientService;
import com.ehealth.mc.service.util.EntityUtil;
import com.ehealth.mc.service.util.McEdmUtil;

@Service("overallService")
@Transactional
public class OverallServiceImpl implements OverallService {

	@Autowired
	private DoctorService dcotorService;

	@Autowired
	private PatientService patientService;

	@Autowired
	private OrderService orderService;

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

	@Override
	public Entity createEntityData(ODataRequest request,
			EdmEntitySet edmEntitySet, Entity requestEntity, OData odata,
			ServiceMetadata edm) throws ODataApplicationException {

		EdmEntityType edmEntityType = edmEntitySet.getEntityType();

		String rawServiceUri = request.getRawBaseUri();

		if (edmEntitySet.getName().equals(McEdmUtil.ES_DOCTORS_NAME)) {
			return createEntity(edmEntitySet, edmEntityType, requestEntity,
					rawServiceUri, odata, edm);
		} else if (edmEntitySet.getName().equals(McEdmUtil.ES_PATIENTS_NAME)) {
			return createEntity(edmEntitySet, edmEntityType, requestEntity,
					rawServiceUri, odata, edm);
		} else if (edmEntitySet.getName().equals(McEdmUtil.ES_ORDERS_NAME)) {
			return createEntity(edmEntitySet, edmEntityType, requestEntity,
					rawServiceUri, odata, edm);
		}

		return null;
	}

	public Entity createEntityData(EdmEntitySet edmEntitySet,
			Entity entityToCreate, String rawServiceUri, OData odata,
			ServiceMetadata edm) throws ODataApplicationException {

		EdmEntityType edmEntityType = edmEntitySet.getEntityType();

		if (edmEntitySet.getName().equals(McEdmUtil.ES_DOCTORS_NAME)) {
			return createEntity(edmEntitySet, edmEntityType, entityToCreate,
					rawServiceUri, odata, edm);
		} else if (edmEntitySet.getName().equals(McEdmUtil.ES_PATIENTS_NAME)) {
			return createEntity(edmEntitySet, edmEntityType, entityToCreate,
					rawServiceUri, odata, edm);
		} else if (edmEntitySet.getName().equals(McEdmUtil.ES_ORDERS_NAME)) {
			return createEntity(edmEntitySet, edmEntityType, entityToCreate,
					rawServiceUri, odata, edm);
		}

		return null;
	}

	public Entity createEntity(EdmEntitySet edmEntitySet,
			EdmEntityType edmEntityType, Entity entity,
			final String rawServiceUri, OData odata, ServiceMetadata edm)
			throws ODataApplicationException {

		Entity createdEntity = null;

		// 1.) Create the entity
		final Entity newEntity = new Entity();
		newEntity.setType(entity.getType());

		// Add all provided properties
		newEntity.getProperties().addAll(entity.getProperties());
		// newEntity.setId(EntityUtil.createId(newEntity, "ID"));

		createdEntity = createEntity(newEntity);

		// 2.1.) Apply binding links
		for (final Link link : entity.getNavigationBindings()) {
			final EdmNavigationProperty edmNavigationProperty = edmEntityType
					.getNavigationProperty(link.getTitle());
			final EdmEntitySet targetEntitySet = (EdmEntitySet) edmEntitySet
					.getRelatedBindingTarget(link.getTitle());

			if (edmNavigationProperty.isCollection()
					&& link.getBindingLinks() != null) {
				for (final String bindingLink : link.getBindingLinks()) {
					final Entity relatedEntity = readEntityByBindingLink(
							bindingLink, targetEntitySet, rawServiceUri, odata,
							edm);
					EntityUtil.createLink(edmNavigationProperty, newEntity,
							relatedEntity);
				}
			} else if (!edmNavigationProperty.isCollection()
					&& link.getBindingLink() != null) {
				final Entity relatedEntity = readEntityByBindingLink(
						link.getBindingLink(), targetEntitySet, rawServiceUri,
						odata, edm);
				EntityUtil.createLink(edmNavigationProperty, newEntity,
						relatedEntity);
			}
		}

		// 2.2.) Create nested entities
		for (final Link link : entity.getNavigationLinks()) {
			final EdmNavigationProperty edmNavigationProperty = edmEntityType
					.getNavigationProperty(link.getTitle());
			final EdmEntitySet targetEntitySet = (EdmEntitySet) edmEntitySet
					.getRelatedBindingTarget(link.getTitle());

			if (edmNavigationProperty.isCollection()
					&& link.getInlineEntitySet() != null) {
				for (final Entity nestedEntity : link.getInlineEntitySet()
						.getEntities()) {
					final Entity newNestedEntity = createEntityData(
							targetEntitySet, nestedEntity, rawServiceUri,
							odata, edm);
					EntityUtil.createLink(edmNavigationProperty, newEntity,
							newNestedEntity);
				}
			} else if (!edmNavigationProperty.isCollection()
					&& link.getInlineEntity() != null) {
				final Entity newNestedEntity = createEntityData(
						targetEntitySet, link.getInlineEntity(), rawServiceUri,
						odata, edm);
				EntityUtil.createLink(edmNavigationProperty, newEntity,
						newNestedEntity);
			}
		}

		return createdEntity;
	}

	private Entity createEntity(final Entity newEntity) {
		if (newEntity.getType().equals(
				McEdmUtil.ET_DOCTOR_FQN.getFullQualifiedNameAsString())) {
			return dcotorService.save(newEntity);
		} else if (newEntity.getType().equals(
				McEdmUtil.ET_PATIENT_FQN.getFullQualifiedNameAsString())) {
			return patientService.save(newEntity);
		} else if (newEntity.getType().equals(
				McEdmUtil.ET_ORDER_FQN.getFullQualifiedNameAsString())) {
			return orderService.save(newEntity);
		}
		return null;
	}

	private Entity readEntityByBindingLink(final String entityId,
			final EdmEntitySet edmEntitySet, final String rawServiceUri,
			OData odata, ServiceMetadata edm) throws ODataApplicationException {

		UriResourceEntitySet entitySetResource = null;
		try {
			entitySetResource = odata.createUriHelper().parseEntityId(
					edm.getEdm(), entityId, rawServiceUri);

			if (!entitySetResource.getEntitySet().getName()
					.equals(edmEntitySet.getName())) {
				throw new ODataApplicationException(
						"Execpted an entity-id for entity set "
								+ edmEntitySet.getName()
								+ " but found id for entity set "
								+ entitySetResource.getEntitySet().getName(),
						HttpStatusCode.BAD_REQUEST.getStatusCode(),
						Locale.ENGLISH);
			}
		} catch (DeserializerException e) {
			throw new ODataApplicationException(entityId
					+ " is not a valid entity-Id",
					HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
		}

		return readEntityData(entitySetResource.getEntitySet(),
				entitySetResource.getKeyPredicates());
	}

	@Override
	public Entity readEntityData(EdmEntitySet edmEntitySet,
			List<UriParameter> keyParams) throws ODataApplicationException {
		if (edmEntitySet.getName().equals(McEdmUtil.ES_DOCTORS_NAME)) {
			Integer idValue = EntityUtil.getID(keyParams);
			if (idValue != null) {
				return dcotorService.findById(idValue.intValue());
			}
		} else if (edmEntitySet.getName().equals(McEdmUtil.ES_PATIENTS_NAME)) {
			Integer idValue = EntityUtil.getID(keyParams);
			if (idValue != null) {
				return patientService.findById(idValue.intValue());
			}
		} else if (edmEntitySet.getName().equals(McEdmUtil.ES_ORDERS_NAME)) {
			Integer idValue = EntityUtil.getID(keyParams);
			if (idValue != null) {
				return orderService.findById(idValue.intValue());
			}
		}
		return null;
	}
}
