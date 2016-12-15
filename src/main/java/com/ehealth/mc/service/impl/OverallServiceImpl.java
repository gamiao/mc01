package com.ehealth.mc.service.impl;

import java.util.Date;
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
import com.ehealth.mc.bo.LoginLog;
import com.ehealth.mc.bo.OrderBilling;
import com.ehealth.mc.bo.OrderConversation;
import com.ehealth.mc.bo.OrderHeader;
import com.ehealth.mc.bo.Patient;
import com.ehealth.mc.service.AdminService;
import com.ehealth.mc.service.DoctorService;
import com.ehealth.mc.service.LoginLogService;
import com.ehealth.mc.service.OrderService;
import com.ehealth.mc.service.OverallService;
import com.ehealth.mc.service.PatientService;
import com.ehealth.mc.service.PaymentService;
import com.ehealth.mc.service.util.EntityConvertUtil;
import com.ehealth.mc.service.util.EntityUtil;
import com.ehealth.mc.service.util.FormatUtil;
import com.ehealth.mc.service.util.McEdmUtil;
import com.google.gson.Gson;

@Service("overallService")
public class OverallServiceImpl implements OverallService {

	private static Gson gson = new Gson();

	@Autowired
	private DoctorService doctorService;

	@Autowired
	private AdminService adminService;

	@Autowired
	private PatientService patientService;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private LoginLogService loginLogService;

	@Override
	public EntityCollection findAll(EdmEntitySet edmEntitySet, UriInfo uriInfo) {
		EntityCollection entityCollection = new EntityCollection();

		String filterStr = null;

		if (uriInfo != null && uriInfo.getFilterOption() != null) {
			FilterOption filterOption = uriInfo.getFilterOption();
			filterStr = filterOption.getText();
		}

		if (McEdmUtil.ES_DOCTORS_NAME.equals(edmEntitySet.getName())) {
			List<Entity> entityList = entityCollection.getEntities();
			String filterString = FormatUtil.getCommonFilterString(filterStr);
			String isDeletedString = FormatUtil.getCommonFilterIsDeleted(filterStr);
			List<Doctor> queryResult = null;

			if (filterString != null && !filterString.isEmpty()) {
				queryResult = doctorService.findByFilterString(filterString);
			} else if (isDeletedString != null && !isDeletedString.isEmpty()) {
				queryResult = doctorService.findByIsDeleted(isDeletedString);
			} else {
				queryResult = doctorService.findAll();
			}
			entityList.addAll(EntityConvertUtil.getDoctorEntityList(queryResult));
			return entityCollection;
		} else if (McEdmUtil.ES_PATIENTS_NAME.equals(edmEntitySet.getName())) {
			List<Entity> entityList = entityCollection.getEntities();
			String filterString = FormatUtil.getCommonFilterString(filterStr);
			String isDeletedString = FormatUtil.getCommonFilterIsDeleted(filterStr);
			List<Patient> queryResult = null;

			if (filterString != null && !filterString.isEmpty()) {
				queryResult = patientService.findByFilterString(filterString);
			} else if (isDeletedString != null && !isDeletedString.isEmpty()) {
				queryResult = patientService.findByIsDeleted(isDeletedString);
			} else {
				queryResult = patientService.findAll();
			}

			entityList.addAll(EntityConvertUtil.getPatientEntityList(queryResult));
			return entityCollection;
		} else if (McEdmUtil.ES_ORDERS_NAME.equals(edmEntitySet.getName())) {

			List<Entity> entityList = entityCollection.getEntities();
			List<OrderHeader> queryResult = null;

			if (filterStr != null && !filterStr.isEmpty()) {
				Long patientID = FormatUtil.getOrderFilterPatientID(filterStr);
				Long doctorID = FormatUtil.getOrderFilterDoctorID(filterStr);
				String isArchivedStr = FormatUtil.getOrderFilterIsArchived(filterStr);
				String isDeletedString = FormatUtil.getCommonFilterIsDeleted(filterStr);
				String status = FormatUtil.getOrderFilterStatus(filterStr);
				if (patientID != null) {
					if ("Y".equals(isArchivedStr) || "N".equals(isArchivedStr)) {
						queryResult = orderService.findByPatientIDAndIsArchived(patientID, isArchivedStr);
					} else {
						queryResult = orderService.findByPatientID(patientID);
					}
				} else if (doctorID != null) {
					if ("pickup".equals(status)) {
						queryResult = orderService.findByDoctorIDForPickUp(doctorID);
					} else if ("Y".equals(isArchivedStr) || "N".equals(isArchivedStr)) {
						queryResult = orderService.findByDoctorIDAndIsArchived(doctorID, isArchivedStr);
					} else {
						queryResult = orderService.findByDoctorID(doctorID);
					}
				} else if (isDeletedString != null) {
					queryResult = orderService.findByIsDeleted(isDeletedString);
				}
			} else {
				queryResult = orderService.findAll();
			}

			if (queryResult != null && queryResult.size() > 0) {
				entityList.addAll(EntityConvertUtil.getOrderEntityList(queryResult));
			}
			return entityCollection;
		}
		return null;
	}

	@Override
	public Entity createEntityData(ODataRequest request, EdmEntitySet edmEntitySet, Entity requestEntity, OData odata,
			ServiceMetadata edm) throws ODataApplicationException {

		EdmEntityType edmEntityType = edmEntitySet.getEntityType();

		String rawServiceUri = request.getRawBaseUri();

		if (edmEntitySet.getName().equals(McEdmUtil.ES_DOCTORS_NAME)) {
			return createEntity(edmEntitySet, edmEntityType, requestEntity, rawServiceUri, odata, edm, null);
		} else if (edmEntitySet.getName().equals(McEdmUtil.ES_PATIENTS_NAME)) {
			return createEntity(edmEntitySet, edmEntityType, requestEntity, rawServiceUri, odata, edm, null);
		} else if (edmEntitySet.getName().equals(McEdmUtil.ES_ORDERS_NAME)) {
			return createEntity(edmEntitySet, edmEntityType, requestEntity, rawServiceUri, odata, edm, null);
		}

		return null;
	}

	public Entity createEntityData(EdmEntitySet edmEntitySet, Entity entityToCreate, String rawServiceUri, OData odata,
			ServiceMetadata edm) throws ODataApplicationException {

		EdmEntityType edmEntityType = edmEntitySet.getEntityType();

		if (edmEntitySet.getName().equals(McEdmUtil.ES_DOCTORS_NAME)) {
			return createEntity(edmEntitySet, edmEntityType, entityToCreate, rawServiceUri, odata, edm, null);
		} else if (edmEntitySet.getName().equals(McEdmUtil.ES_PATIENTS_NAME)) {
			return createEntity(edmEntitySet, edmEntityType, entityToCreate, rawServiceUri, odata, edm, null);
		} else if (edmEntitySet.getName().equals(McEdmUtil.ES_ORDERS_NAME)) {
			return createEntity(edmEntitySet, edmEntityType, entityToCreate, rawServiceUri, odata, edm, null);
		}

		return null;
	}

	public Entity createEntity(EdmEntitySet edmEntitySet, EdmEntityType edmEntityType, Entity entity,
			final String rawServiceUri, OData odata, ServiceMetadata edm, Entity parentEntity)
					throws ODataApplicationException {

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
			final EdmNavigationProperty edmNavigationProperty = edmEntityType.getNavigationProperty(link.getTitle());
			final EdmEntitySet targetEntitySet = (EdmEntitySet) edmEntitySet.getRelatedBindingTarget(link.getTitle());

			if (edmNavigationProperty.isCollection() && link.getBindingLinks() != null) {
				for (final String bindingLink : link.getBindingLinks()) {
					final Entity relatedEntity = readEntityByBindingLink(bindingLink, targetEntitySet, rawServiceUri,
							odata, edm);
					EntityUtil.createLink(edmNavigationProperty, newEntity, relatedEntity);
				}
			} else if (!edmNavigationProperty.isCollection() && link.getBindingLink() != null) {
				final Entity relatedEntity = readEntityByBindingLink(link.getBindingLink(), targetEntitySet,
						rawServiceUri, odata, edm);
				EntityUtil.createLink(edmNavigationProperty, newEntity, relatedEntity);
			}
		}

		// 2.2.) Create nested entities
		for (final Link link : entity.getNavigationLinks()) {
			final EdmNavigationProperty edmNavigationProperty = edmEntityType.getNavigationProperty(link.getTitle());
			final EdmEntitySet targetEntitySet = (EdmEntitySet) edmEntitySet.getRelatedBindingTarget(link.getTitle());

			if (edmNavigationProperty.isCollection() && link.getInlineEntitySet() != null) {
				for (final Entity nestedEntity : link.getInlineEntitySet().getEntities()) {
					final Entity newNestedEntity = createEntityData(targetEntitySet, nestedEntity, rawServiceUri, odata,
							edm);
					EntityUtil.createLink(edmNavigationProperty, newEntity, newNestedEntity);
				}
			} else if (!edmNavigationProperty.isCollection() && link.getInlineEntity() != null) {
				final Entity newNestedEntity = createEntityData(targetEntitySet, link.getInlineEntity(), rawServiceUri,
						odata, edm);
				EntityUtil.createLink(edmNavigationProperty, newEntity, newNestedEntity);
			}
		}

		return createdEntity;
	}

	private Entity createEntity(final Entity newEntity) {
		if (newEntity.getType().equals(McEdmUtil.ET_DOCTOR_FQN.getFullQualifiedNameAsString())) {
			return EntityConvertUtil.getEntity(doctorService.upsertBasicInfo(newEntity));
		} else if (newEntity.getType().equals(McEdmUtil.ET_PATIENT_FQN.getFullQualifiedNameAsString())) {
			return EntityConvertUtil.getEntity(patientService.upsertBasicInfo(newEntity));
		} else if (newEntity.getType().equals(McEdmUtil.ET_ORDER_FQN.getFullQualifiedNameAsString())) {
			return EntityConvertUtil.getEntity(orderService.create(newEntity));
		}
		return null;
	}

	private Entity createEntity(final Entity newEntity, final Entity parentEntity) {
		if (parentEntity.getType().equals(McEdmUtil.ET_ORDER_FQN.getFullQualifiedNameAsString())) {
			if (newEntity.getType().equals(McEdmUtil.ET_ORDER_CONV_FQN.getFullQualifiedNameAsString())) {
				return EntityConvertUtil.getEntity(orderService.createOrderConversaction(newEntity, parentEntity));
			}
		}
		return null;
	}

	private Entity updateEntity(final Entity updateEntity) {
		if (updateEntity.getType().equals(McEdmUtil.ET_DOCTOR_FQN.getFullQualifiedNameAsString())) {
			return EntityConvertUtil.getEntity(doctorService.upsertBasicInfo(updateEntity));
		} else if (updateEntity.getType().equals(McEdmUtil.ET_PATIENT_FQN.getFullQualifiedNameAsString())) {
			return EntityConvertUtil.getEntity(patientService.upsertBasicInfo(updateEntity));
		} else if (updateEntity.getType().equals(McEdmUtil.ET_ORDER_FQN.getFullQualifiedNameAsString())) {
			return EntityConvertUtil.getEntity(orderService.update(updateEntity));
		}
		return null;
	}

	private Entity readEntityByBindingLink(final String entityId, final EdmEntitySet edmEntitySet,
			final String rawServiceUri, OData odata, ServiceMetadata edm) throws ODataApplicationException {

		UriResourceEntitySet entitySetResource = null;
		try {
			entitySetResource = odata.createUriHelper().parseEntityId(edm.getEdm(), entityId, rawServiceUri);

			if (!entitySetResource.getEntitySet().getName().equals(edmEntitySet.getName())) {
				throw new ODataApplicationException(
						"Execpted an entity-id for entity set " + edmEntitySet.getName()
								+ " but found id for entity set " + entitySetResource.getEntitySet().getName(),
						HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
			}
		} catch (DeserializerException e) {
			throw new ODataApplicationException(entityId + " is not a valid entity-Id",
					HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
		}

		return readEntityData(entitySetResource.getEntitySet(), entitySetResource.getKeyPredicates());
	}

	@Override
	public Entity readEntityData(EdmEntitySet edmEntitySet, List<UriParameter> keyParams)
			throws ODataApplicationException {

		// sendMailTest();
		if (edmEntitySet.getName().equals(McEdmUtil.ES_DOCTORS_NAME)) {
			Long idValue = EntityUtil.getID(keyParams);
			if (idValue != null) {
				return EntityConvertUtil.getEntity(doctorService.findById(idValue.longValue()));
			}
		} else if (edmEntitySet.getName().equals(McEdmUtil.ES_PATIENTS_NAME)) {
			Long idValue = EntityUtil.getID(keyParams);
			if (idValue != null) {
				return EntityConvertUtil.getEntity(patientService.findById(idValue.longValue()));
			}
		} else if (edmEntitySet.getName().equals(McEdmUtil.ES_ORDERS_NAME)) {
			Long idValue = EntityUtil.getID(keyParams);
			if (idValue != null) {
				return EntityConvertUtil.getEntity(orderService.findById(idValue.longValue()));
			}
		}
		return null;
	}

	@Override
	public void updateEntityData(ODataRequest request, List<UriParameter> keyParams, EdmEntitySet edmEntitySet,
			Entity updateEntity, OData odata, ServiceMetadata edm) throws ODataApplicationException {
		HttpMethod httpMethod = request.getMethod();
		if (edmEntitySet.getName().equals(McEdmUtil.ES_DOCTORS_NAME)) {
			updateEntity(edmEntitySet, keyParams, updateEntity, httpMethod);
		} else if (edmEntitySet.getName().equals(McEdmUtil.ES_PATIENTS_NAME)) {
			updateEntity(edmEntitySet, keyParams, updateEntity, httpMethod);
		} else if (edmEntitySet.getName().equals(McEdmUtil.ES_ORDERS_NAME)) {
			updateEntity(edmEntitySet, keyParams, updateEntity, httpMethod);
		}

	}

	private void updateEntity(EdmEntitySet edmEntitySet, List<UriParameter> keyParams, Entity updateEntity,
			HttpMethod httpMethod) throws ODataApplicationException {

		EdmEntityType edmEntityType = edmEntitySet.getEntityType();

		Entity entity = readEntityData(edmEntitySet, keyParams);
		if (entity == null) {
			throw new ODataApplicationException("Entity not found", HttpStatusCode.NOT_FOUND.getStatusCode(),
					Locale.ENGLISH);
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
			existingProp.setValue(existingProp.getValueType(), updateProperty.getValue());
		}

		updateEntity(entity);
	}

	@Override
	public String updateEntityAfterFileUploaded(String entityType, String entityID, String method, String fileName) {
		Long entityLongID = EntityConvertUtil.getLong(entityID);
		if (entityType != null && entityLongID != null) {

			if (entityType.equals("Patient")) {
				Patient targetPatient = patientService.updateAvatar(fileName, entityLongID);
				if (targetPatient != null) {
					return targetPatient.getAvatar();
				}

			} else if (entityType.equals("Doctor")) {
				Doctor targetDoctor = doctorService.updateAvatar(fileName, entityLongID);
				if (targetDoctor != null) {
					return targetDoctor.getAvatar();
				}

			} else if (entityType.equals("OrderConv")) {
				if ("P".equals(method) || "D".equals(method)) {
					OrderConversation orderConversation = orderService.createImageOrderConversaction(fileName,
							entityLongID, method);
					return orderConversation.getPictures();
				}
			}
		}

		return null;

	}

	@Override
	public Entity createCascatedEntityData(Entity parentEntity, ODataRequest request, EdmEntitySet edmEntitySet,
			Entity requestEntity, OData odata, ServiceMetadata edm) throws ODataApplicationException {

		EdmEntityType edmEntityType = edmEntitySet.getEntityType();
		String rawServiceUri = request.getRawBaseUri();
		if (edmEntitySet.getName().equals(McEdmUtil.ES_ORDER_CONVS_NAME)) {
			return createEntity(edmEntitySet, edmEntityType, requestEntity, rawServiceUri, odata, edm, parentEntity);
		}
		return null;
	}

	@Override
	public Long getLoginUserID(String loginType, String login, String password, String ip, String userAgent) {
		if ("S".equals(loginType)) {
			Long id = adminService.findOneByLoginAndPassword(login, password);
			if (id != null) {
				createLoginLog(loginType, login, password, ip, userAgent, "S", gson.toJson(id));
				return id;
			}
		} else if ("D".equals(loginType)) {
			Doctor user = doctorService.findOneByLoginAndPassword(login, password);
			if (user != null) {
				createLoginLog(loginType, login, password, ip, userAgent, "S", gson.toJson(user));
				return user.getId();
			}
		} else if ("P".equals(loginType)) {
			Patient user = patientService.findOneByLoginAndPassword(login, password);
			if (user != null) {
				createLoginLog(loginType, login, password, ip, userAgent, "S", gson.toJson(user));
				return user.getId();
			}
		}

		createLoginLog(loginType, login, password, ip, userAgent, "E", null);
		return null;
	}

	private LoginLog createLoginLog(String loginType, String login, String password, String ip, String userAgent,
			String result, String resultContent) {
		LoginLog loginLog = new LoginLog();
		loginLog.setCreateTime(new Date());
		loginLog.setIp(ip);
		loginLog.setLogin(login);
		loginLog.setPassword(password);
		loginLog.setResult(result);
		loginLog.setResultContent(resultContent);
		loginLog.setUa(userAgent);
		loginLog.setType(loginType);
		return loginLogService.create(loginLog);
	}

	@Override
	public Long updatePassword(String loginType, Long id, String oldPassword, String newPassword) {
		if ("S".equals(loginType)) {
			// not support for admin
		} else if ("D".equals(loginType)) {
			Doctor user = doctorService.updatePassword(newPassword, id, oldPassword);
			if (user != null) {
				return user.getId();
			}
		} else if ("P".equals(loginType)) {
			Patient user = patientService.updatePassword(newPassword, id, oldPassword);
			if (user != null) {
				return user.getId();
			}
		}
		return null;
	}

	@Override
	public boolean checkLogin(String loginType, String login) {
		if ("S".equals(loginType)) {
			// not support for admin
		} else if ("D".equals(loginType)) {
			Doctor user = doctorService.findOneByLogin(login);
			if (user != null) {
				return false;
			}
		} else if ("P".equals(loginType)) {
			Patient user = patientService.findOneByLogin(login);
			if (user != null) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean reopenOrders(Long[] objectIDs) {
		return orderService.reopenOrders(objectIDs);
	}

	@Override
	public boolean completeOrders(Long[] objectIDs) {
		return orderService.completeOrders(objectIDs);
	}

	@Override
	public boolean setIsDeleted(String objType, String value, Long[] objectIDs) {
		if ("Patient".equals(objType)) {
			try {
				boolean result = patientService.updateIsDeleted(value, objectIDs);
				return result;
			} catch (RuntimeException e) {
				return false;
			}
		} else if ("Doctor".equals(objType)) {
			try {
				boolean result = doctorService.updateIsDeleted(value, objectIDs);
				return result;
			} catch (RuntimeException e) {
				return false;
			}
		} else if ("Order".equals(objType)) {
			try {
				boolean result = orderService.updateIsDeleted(value, objectIDs);
				return result;
			} catch (RuntimeException e) {
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean setIsArchived(String objType, String value, Long[] objectIDs) {
		if ("Order".equals(objType)) {
			try {
				boolean result = orderService.updateIsArchived(value, objectIDs);
				return result;
			} catch (RuntimeException e) {
				return false;
			}
		}
		return false;
	}

	@Override
	public OrderBilling createOrderBillingByOrderID(String orderID) {
		return orderService.createOrderBillingByOrderID(orderID);
	}

	@Override
	public String getPayForm(OrderBilling orderBilling) {
		return paymentService.getPayForm(orderBilling);
	}

	@Override
	public boolean mailPassword(String mail, String loginType) {

		if ("D".equals(loginType)) {
			if (doctorService.mailAccountInfoByMail(mail)) {
				return true;
			}
		} else if ("P".equals(loginType)) {
			if (patientService.mailAccountInfoByMail(mail)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Integer completeAllNoResponseOrder() {
		return orderService.completeAllNoResponseOrder();
	}
}
