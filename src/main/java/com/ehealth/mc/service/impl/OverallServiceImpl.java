package com.ehealth.mc.service.impl;

import java.util.List;
import java.util.Locale;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Link;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmNavigationProperty;
import org.apache.olingo.commons.api.http.HttpMethod;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.ODataRequest;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.deserializer.DeserializerException;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;
import org.apache.olingo.server.api.uri.queryoption.FilterOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehealth.mc.bo.Doctor;
import com.ehealth.mc.bo.OrderHeader;
import com.ehealth.mc.bo.Patient;
import com.ehealth.mc.service.DoctorService;
import com.ehealth.mc.service.OrderService;
import com.ehealth.mc.service.OverallService;
import com.ehealth.mc.service.PatientService;
import com.ehealth.mc.service.util.EntityConvertUtil;
import com.ehealth.mc.service.util.EntityUtil;
import com.ehealth.mc.service.util.FormatUtil;
import com.ehealth.mc.service.util.McEdmUtil;

@Service("overallService")
public class OverallServiceImpl implements OverallService {

	@Autowired
	private DoctorService doctorService;

	@Autowired
	private PatientService patientService;

	@Autowired
	private OrderService orderService;

	@Override
	public EntityCollection findAll(EdmEntitySet edmEntitySet, UriInfo uriInfo) {
		EntityCollection entityCollection = new EntityCollection();

		if (McEdmUtil.ES_DOCTORS_NAME.equals(edmEntitySet.getName())) {
			List<Entity> entityList = entityCollection.getEntities();
			List<Doctor> queryResult = doctorService.findByIsDeleted("N");
			entityList.addAll(EntityConvertUtil
					.getDoctorEntityList(queryResult));
			return entityCollection;
		} else if (McEdmUtil.ES_ORDERS_NAME.equals(edmEntitySet.getName())) {

			FilterOption filterOption = null;

			if (uriInfo != null) {
				filterOption = uriInfo.getFilterOption();
			}

			List<Entity> entityList = entityCollection.getEntities();
			List<OrderHeader> queryResult = null;

			if (filterOption != null && filterOption.getText() != null) {
				String filterStr = filterOption.getText();
				Long patientID = FormatUtil.getOrderFilterPatientID(filterStr);
				Long doctorID = FormatUtil.getOrderFilterDoctorID(filterStr);
				String isArchivedStr = FormatUtil
						.getOrderFilterIsArchived(filterStr);
				String status = FormatUtil.getOrderFilterStatus(filterStr);
				if (patientID != null) {
					if (isArchivedStr != null) {
						if ("Y".endsWith(isArchivedStr)) {
							queryResult = orderService
									.findByPatientIDArchived(patientID);
						} else {
							queryResult = orderService
									.findByPatientIDNotArchived(patientID);
						}
					} else {
						queryResult = orderService.findByPatientID(patientID);
					}
				} else if (doctorID != null) {
					if ("pickup".equals(status)) {
						queryResult = orderService
								.findByDoctorIDForPickUp(doctorID);
					} else if (isArchivedStr != null) {
						if ("Y".endsWith(isArchivedStr)) {
							queryResult = orderService
									.findByDoctorIDArchived(doctorID);
						} else {
							queryResult = orderService
									.findByDoctorIDNotArchived(doctorID);
						}
					} else {
						queryResult = orderService.findByDoctorID(doctorID);
					}
				}
			} else {
				queryResult = orderService.findAll();
			}

			if (queryResult != null && queryResult.size() > 0) {
				entityList.addAll(EntityConvertUtil
						.getOrderEntityList(queryResult));
			}
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
					rawServiceUri, odata, edm, null);
		} else if (edmEntitySet.getName().equals(McEdmUtil.ES_PATIENTS_NAME)) {
			return createEntity(edmEntitySet, edmEntityType, requestEntity,
					rawServiceUri, odata, edm, null);
		} else if (edmEntitySet.getName().equals(McEdmUtil.ES_ORDERS_NAME)) {
			return createEntity(edmEntitySet, edmEntityType, requestEntity,
					rawServiceUri, odata, edm, null);
		}

		return null;
	}

	public Entity createEntityData(EdmEntitySet edmEntitySet,
			Entity entityToCreate, String rawServiceUri, OData odata,
			ServiceMetadata edm) throws ODataApplicationException {

		EdmEntityType edmEntityType = edmEntitySet.getEntityType();

		if (edmEntitySet.getName().equals(McEdmUtil.ES_DOCTORS_NAME)) {
			return createEntity(edmEntitySet, edmEntityType, entityToCreate,
					rawServiceUri, odata, edm, null);
		} else if (edmEntitySet.getName().equals(McEdmUtil.ES_PATIENTS_NAME)) {
			return createEntity(edmEntitySet, edmEntityType, entityToCreate,
					rawServiceUri, odata, edm, null);
		} else if (edmEntitySet.getName().equals(McEdmUtil.ES_ORDERS_NAME)) {
			return createEntity(edmEntitySet, edmEntityType, entityToCreate,
					rawServiceUri, odata, edm, null);
		}

		return null;
	}

	public Entity createEntity(EdmEntitySet edmEntitySet,
			EdmEntityType edmEntityType, Entity entity,
			final String rawServiceUri, OData odata, ServiceMetadata edm,
			Entity parentEntity) throws ODataApplicationException {

		Entity createdEntity = null;

		// 1.) Create the entity
		final Entity newEntity = new Entity();
		newEntity.setType(entity.getType());

		// Add all provided properties
		newEntity.getProperties().addAll(entity.getProperties());
		// newEntity.setId(EntityUtil.createId(newEntity, "ID"));

		if (parentEntity == null) {

			createdEntity = createEntity(newEntity);
		} else {

			createdEntity = createEntity(newEntity, parentEntity);
		}
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
			return EntityConvertUtil.getEntity(doctorService
					.upsertBasicInfo(newEntity));
		} else if (newEntity.getType().equals(
				McEdmUtil.ET_PATIENT_FQN.getFullQualifiedNameAsString())) {
			return EntityConvertUtil.getEntity(patientService
					.upsertBasicInfo(newEntity));
		} else if (newEntity.getType().equals(
				McEdmUtil.ET_ORDER_FQN.getFullQualifiedNameAsString())) {
			return EntityConvertUtil.getEntity(orderService.create(newEntity));
		}
		return null;
	}

	private Entity createEntity(final Entity newEntity,
			final Entity parentEntity) {
		if (parentEntity.getType().equals(
				McEdmUtil.ET_ORDER_FQN.getFullQualifiedNameAsString())) {
			if (newEntity.getType().equals(
					McEdmUtil.ET_ORDER_CONV_FQN.getFullQualifiedNameAsString())) {
				return EntityConvertUtil.getEntity(orderService
						.createOrderConversaction(newEntity, parentEntity));
			}
		}
		return null;
	}

	private Entity updateEntity(final Entity updateEntity) {
		if (updateEntity.getType().equals(
				McEdmUtil.ET_DOCTOR_FQN.getFullQualifiedNameAsString())) {
			return EntityConvertUtil.getEntity(doctorService
					.upsertBasicInfo(updateEntity));
		} else if (updateEntity.getType().equals(
				McEdmUtil.ET_PATIENT_FQN.getFullQualifiedNameAsString())) {
			return EntityConvertUtil.getEntity(patientService
					.upsertBasicInfo(updateEntity));
		} else if (updateEntity.getType().equals(
				McEdmUtil.ET_ORDER_FQN.getFullQualifiedNameAsString())) {
			return EntityConvertUtil.getEntity(orderService
					.update(updateEntity));
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
			Long idValue = EntityUtil.getID(keyParams);
			if (idValue != null) {
				return EntityConvertUtil.getEntity(doctorService
						.findById(idValue.longValue()));
			}
		} else if (edmEntitySet.getName().equals(McEdmUtil.ES_PATIENTS_NAME)) {
			Long idValue = EntityUtil.getID(keyParams);
			if (idValue != null) {
				return EntityConvertUtil.getEntity(patientService
						.findById(idValue.longValue()));
			}
		} else if (edmEntitySet.getName().equals(McEdmUtil.ES_ORDERS_NAME)) {
			Long idValue = EntityUtil.getID(keyParams);
			if (idValue != null) {
				return EntityConvertUtil.getEntity(orderService
						.findById(idValue.longValue()));
			}
		}
		return null;
	}

	@Override
	public void updateEntityData(ODataRequest request,
			List<UriParameter> keyParams, EdmEntitySet edmEntitySet,
			Entity updateEntity, OData odata, ServiceMetadata edm)
			throws ODataApplicationException {
		HttpMethod httpMethod = request.getMethod();
		if (edmEntitySet.getName().equals(McEdmUtil.ES_DOCTORS_NAME)) {
			updateEntity(edmEntitySet, keyParams, updateEntity, httpMethod);
		} else if (edmEntitySet.getName().equals(McEdmUtil.ES_PATIENTS_NAME)) {
			updateEntity(edmEntitySet, keyParams, updateEntity, httpMethod);
		} else if (edmEntitySet.getName().equals(McEdmUtil.ES_ORDERS_NAME)) {
			updateEntity(edmEntitySet, keyParams, updateEntity, httpMethod);
		}

	}

	private void updateEntity(EdmEntitySet edmEntitySet,
			List<UriParameter> keyParams, Entity updateEntity,
			HttpMethod httpMethod) throws ODataApplicationException {

		EdmEntityType edmEntityType = edmEntitySet.getEntityType();

		Entity entity = readEntityData(edmEntitySet, keyParams);
		if (entity == null) {
			throw new ODataApplicationException("Entity not found",
					HttpStatusCode.NOT_FOUND.getStatusCode(), Locale.ENGLISH);
		}

		// loop over all properties and replace the values with the values of
		// the given payload
		// Note: ignoring ComplexType, as we don't have it in our odata model
		List<Property> existingProperties = entity.getProperties();
		entity.setType(updateEntity.getType());
		for (Property existingProp : existingProperties) {
			String propName = existingProp.getName();

			// ignore the key properties, they aren't updateable
			if (EntityUtil.isKey(edmEntityType, propName)) {
				continue;
			}

			Property updateProperty = updateEntity.getProperty(propName);
			// the request payload might not consider ALL properties, so it can
			// be null
			if (updateProperty == null) {
				// if a property has NOT been added to the request payload
				// depending on the HttpMethod, our behavior is different
				if (httpMethod.equals(HttpMethod.PATCH)) {
					// as of the OData spec, in case of PATCH, the existing
					// property is not touched
					continue; // do nothing
				} else if (httpMethod.equals(HttpMethod.PUT)) {
					// as of the OData spec, in case of PUT, the existing
					// property is set to null (or to default value)
					existingProp.setValue(existingProp.getValueType(), null);
					continue;
				}
			}

			// change the value of the properties
			existingProp.setValue(existingProp.getValueType(),
					updateProperty.getValue());
		}

		updateEntity(entity);
	}

	@Override
	public String updateEntityAfterFileUploaded(String entityType,
			String entityID, String method, String fileName) {
		Long entityLongID = EntityConvertUtil.getLong(entityID);
		if (entityType != null && entityLongID != null) {

			if (entityType.equals("Patient")) {
				Patient targetPatient = patientService.updateAvatar(fileName,
						entityLongID);
				if (targetPatient != null) {
					return targetPatient.getAvatar();
				}

			} else if (entityType.equals("Doctor")) {
				Doctor targetDoctor = doctorService.updateAvatar(fileName,
						entityLongID);
				if (targetDoctor != null) {
					return targetDoctor.getAvatar();
				}

			} else if (entityType.equals("OrderConv")) {

			}

		}

		return null;

	}

	@Override
	public Entity createCascatedEntityData(Entity parentEntity,
			ODataRequest request, EdmEntitySet edmEntitySet,
			Entity requestEntity, OData odata, ServiceMetadata edm)
			throws ODataApplicationException {

		EdmEntityType edmEntityType = edmEntitySet.getEntityType();
		String rawServiceUri = request.getRawBaseUri();
		if (edmEntitySet.getName().equals(McEdmUtil.ES_ORDER_CONVS_NAME)) {
			return createEntity(edmEntitySet, edmEntityType, requestEntity,
					rawServiceUri, odata, edm, parentEntity);
		}
		return null;
	}
}
