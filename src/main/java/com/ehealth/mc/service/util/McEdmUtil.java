package com.ehealth.mc.service.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlComplexType;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainer;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainerInfo;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlNavigationProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlNavigationPropertyBinding;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef;
import org.apache.olingo.commons.api.edm.provider.CsdlSchema;

public class McEdmUtil {

	// Service Namespace
	public static final String NAMESPACE = "OData.Mc";
	// EDM Container
	public static final String CONTAINER_NAME = "Container";
	public static final FullQualifiedName CONTAINER = new FullQualifiedName(
			NAMESPACE, CONTAINER_NAME);

	// Entity Types Names
	public static final String ET_DOCTOR_NAME = "Doctor";
	public static final FullQualifiedName ET_DOCTOR_FQN = new FullQualifiedName(
			NAMESPACE, ET_DOCTOR_NAME);
	public static final String ET_PATIENT_NAME = "Patient";
	public static final FullQualifiedName ET_PATIENT_FQN = new FullQualifiedName(
			NAMESPACE, ET_PATIENT_NAME);
	public static final String ET_ORDER_DETAIL_NAME = "OrderDetail";
	public static final FullQualifiedName ET_ORDER_DETAIL_FQN = new FullQualifiedName(
			NAMESPACE, ET_ORDER_DETAIL_NAME);
	public static final String ET_ORDER_NAME = "Order";
	public static final FullQualifiedName ET_ORDER_FQN = new FullQualifiedName(
			NAMESPACE, ET_ORDER_NAME);
	public static final String ET_ORDER_CONV_NAME = "OrderConv";
	public static final FullQualifiedName ET_ORDER_CONV_FQN = new FullQualifiedName(
			NAMESPACE, ET_ORDER_CONV_NAME);

	// Entity Set Names
	public static final String ES_DOCTORS_NAME = "Doctors";
	public static final String ES_PATIENTS_NAME = "Patients";
	public static final String ES_ORDERS_NAME = "Orders";
	public static final String ES_ORDER_DETAILS_NAME = "OrderDetails";
	public static final String ES_ORDER_CONVS_NAME = "OrderConvs";

	// Complex Type Names
	public static final String CT_DOCTOR_NAME = "CTDoctor";
	public static final String CT_PATIENT_NAME = "CTPatient";
	public static final String CT_ORDER_DETAIL_NAME = "CTOrderDetail";
	public static final FullQualifiedName CT_DOCTOR_FQN = new FullQualifiedName(
			NAMESPACE, CT_DOCTOR_NAME);
	public static final FullQualifiedName CT_PATIENT_FQN = new FullQualifiedName(
			NAMESPACE, CT_PATIENT_NAME);
	public static final FullQualifiedName CT_ORDER_DETAIL_FQN = new FullQualifiedName(
			NAMESPACE, CT_ORDER_DETAIL_NAME);

	// Entity configurations
	public static final Map<String, String> EntityToSetMap = new HashMap<String, String>();
	public static final List<CsdlEntityType> AllEntityTypeFQN = new ArrayList<CsdlEntityType>();
	public static final List<CsdlComplexType> AllComplexTypeFQN = new ArrayList<CsdlComplexType>();
	public static final List<CsdlEntitySet> AllEntitySetFQN = new ArrayList<CsdlEntitySet>();

	static {
		AllEntityTypeFQN.add(getEntityType(ET_DOCTOR_FQN));
		AllEntityTypeFQN.add(getEntityType(ET_PATIENT_FQN));
		AllEntityTypeFQN.add(getEntityType(ET_ORDER_DETAIL_FQN));
		AllEntityTypeFQN.add(getEntityType(ET_ORDER_FQN));
		AllEntityTypeFQN.add(getEntityType(ET_ORDER_CONV_FQN));

		AllComplexTypeFQN.add(getComplexType(CT_DOCTOR_FQN));
		AllComplexTypeFQN.add(getComplexType(CT_PATIENT_FQN));
		AllComplexTypeFQN.add(getComplexType(CT_ORDER_DETAIL_FQN));

		AllEntitySetFQN.add(getEntitySet(CONTAINER, ES_DOCTORS_NAME));
		AllEntitySetFQN.add(getEntitySet(CONTAINER, ES_PATIENTS_NAME));
		AllEntitySetFQN.add(getEntitySet(CONTAINER, ES_ORDER_DETAILS_NAME));
		AllEntitySetFQN.add(getEntitySet(CONTAINER, ES_ORDERS_NAME));
		AllEntitySetFQN.add(getEntitySet(CONTAINER, ES_ORDER_CONVS_NAME));

		EntityToSetMap.put(ET_DOCTOR_NAME, ES_DOCTORS_NAME);
		EntityToSetMap.put(ET_PATIENT_NAME, ES_PATIENTS_NAME);
		EntityToSetMap.put(ET_ORDER_DETAIL_NAME, ES_ORDER_DETAILS_NAME);
		EntityToSetMap.put(ET_ORDER_NAME, ES_ORDERS_NAME);
		EntityToSetMap.put(ET_ORDER_CONV_NAME, ES_ORDER_CONVS_NAME);
	}

	public static final CsdlEntityContainerInfo getEntityContainerInfo(
			FullQualifiedName entityContainerName) {
		// This method is invoked when displaying the service document
		if (entityContainerName == null
				|| entityContainerName.equals(CONTAINER)) {
			CsdlEntityContainerInfo entityContainerInfo = new CsdlEntityContainerInfo();
			entityContainerInfo.setContainerName(CONTAINER);
			return entityContainerInfo;
		}

		return null;
	}

	public static final List<CsdlSchema> getSchemas() {
		// create Schema
		CsdlSchema schema = new CsdlSchema();
		schema.setNamespace(NAMESPACE);

		// add EntityTypes
		List<CsdlEntityType> entityTypes = new ArrayList<CsdlEntityType>();
		entityTypes.addAll(AllEntityTypeFQN);
		schema.setEntityTypes(entityTypes);

		// ComplexTypes
		List<CsdlComplexType> complexTypes = new ArrayList<CsdlComplexType>();
		complexTypes.addAll(AllComplexTypeFQN);
		schema.setComplexTypes(complexTypes);

		// add EntityContainer
		schema.setEntityContainer(getEntityContainer());

		// finally
		List<CsdlSchema> schemas = new ArrayList<CsdlSchema>();
		schemas.add(schema);

		return schemas;
	}

	public static final CsdlEntityType getEntityType(
			FullQualifiedName entityTypeName) {
		// is called for one of the EntityTypes configured in the Schema
		if (entityTypeName.equals(ET_DOCTOR_FQN)) {
			CsdlProperty id = new CsdlProperty().setName("ID").setType(
					EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
			CsdlProperty login = new CsdlProperty().setName("Login").setType(
					EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty name = new CsdlProperty().setName("Name").setType(
					EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty createTime = new CsdlProperty().setName("CreateTime")
					.setType(EdmPrimitiveTypeKind.Date.getFullQualifiedName());
			CsdlProperty gender = new CsdlProperty().setName("Gender").setType(
					EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty avatar = new CsdlProperty().setName("Avatar").setType(
					EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty mail = new CsdlProperty().setName("Mail").setType(
					EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty address = new CsdlProperty()
					.setName("Address")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty mobile = new CsdlProperty().setName("Mobile").setType(
					EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty birthday = new CsdlProperty().setName("Birthday")
					.setType(EdmPrimitiveTypeKind.Date.getFullQualifiedName());
			CsdlProperty medicalLevel = new CsdlProperty().setName(
					"MedicalLevel").setType(
					EdmPrimitiveTypeKind.String.getFullQualifiedName());

			// create CsdlPropertyRef for Key element
			CsdlPropertyRef propertyRef = new CsdlPropertyRef();
			propertyRef.setName("ID");

			// configure EntityType
			CsdlEntityType entityType = new CsdlEntityType();
			entityType.setName(ES_DOCTORS_NAME);
			entityType.setProperties(Arrays.asList(id, login, name, createTime,
					gender, avatar, mail, address, mobile, birthday,
					medicalLevel));
			entityType.setKey(Collections.singletonList(propertyRef));

			return entityType;
		} else if (entityTypeName.equals(ET_PATIENT_FQN)) {
			CsdlProperty id = new CsdlProperty().setName("ID").setType(
					EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
			CsdlProperty login = new CsdlProperty().setName("Login").setType(
					EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty name = new CsdlProperty().setName("Name").setType(
					EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty createTime = new CsdlProperty().setName("CreateTime")
					.setType(EdmPrimitiveTypeKind.Date.getFullQualifiedName());
			CsdlProperty gender = new CsdlProperty().setName("Gender").setType(
					EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty avatar = new CsdlProperty().setName("Avatar").setType(
					EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty mail = new CsdlProperty().setName("Mail").setType(
					EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty address = new CsdlProperty()
					.setName("Address")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty mobile = new CsdlProperty().setName("Mobile").setType(
					EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty birthday = new CsdlProperty().setName("Birthday")
					.setType(EdmPrimitiveTypeKind.Date.getFullQualifiedName());

			// create CsdlPropertyRef for Key element
			CsdlPropertyRef propertyRef = new CsdlPropertyRef();
			propertyRef.setName("ID");

			// configure EntityType
			CsdlEntityType entityType = new CsdlEntityType();
			entityType.setName(ES_PATIENTS_NAME);
			entityType.setProperties(Arrays.asList(id, login, name, createTime,
					gender, avatar, mail, address, mobile, birthday));
			entityType.setKey(Collections.singletonList(propertyRef));

			return entityType;
		} else if (entityTypeName.equals(ET_ORDER_DETAIL_FQN)) {
			CsdlProperty id = new CsdlProperty().setName("ID").setType(
					EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
			CsdlProperty type = new CsdlProperty().setName("Type").setType(
					EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty createTime = new CsdlProperty().setName("CreateTime")
					.setType(EdmPrimitiveTypeKind.Date.getFullQualifiedName());
			CsdlProperty title = new CsdlProperty().setName("Title").setType(
					EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty description = new CsdlProperty()
					.setName("Description").setType(
							EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty pictures = new CsdlProperty().setName("Pics").setType(
					EdmPrimitiveTypeKind.String.getFullQualifiedName());

			// create CsdlPropertyRef for Key element
			CsdlPropertyRef propertyRef = new CsdlPropertyRef();
			propertyRef.setName("ID");

			// configure EntityType
			CsdlEntityType entityType = new CsdlEntityType();
			entityType.setName(ES_ORDER_DETAILS_NAME);
			entityType.setProperties(Arrays.asList(id, type, createTime, title,
					description, pictures));
			entityType.setKey(Collections.singletonList(propertyRef));

			return entityType;
		} else if (entityTypeName.equals(ET_ORDER_CONV_FQN)) {
			CsdlProperty id = new CsdlProperty().setName("ID").setType(
					EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
			CsdlProperty type = new CsdlProperty().setName("Type").setType(
					EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty createTime = new CsdlProperty().setName("CreateTime")
					.setType(EdmPrimitiveTypeKind.Date.getFullQualifiedName());
			CsdlProperty title = new CsdlProperty().setName("Title").setType(
					EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty description = new CsdlProperty()
					.setName("Description").setType(
							EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty pictures = new CsdlProperty().setName("Pics").setType(
					EdmPrimitiveTypeKind.String.getFullQualifiedName());

			// create CsdlPropertyRef for Key element
			CsdlPropertyRef propertyRef = new CsdlPropertyRef();
			propertyRef.setName("ID");

			// configure EntityType
			CsdlEntityType entityType = new CsdlEntityType();
			entityType.setName(ES_ORDER_CONVS_NAME);
			entityType.setProperties(Arrays.asList(id, type, createTime, title,
					description, pictures));
			entityType.setKey(Collections.singletonList(propertyRef));

			return entityType;
		} else if (entityTypeName.equals(ET_ORDER_FQN)) {
			// create EntityType properties
			CsdlProperty id = new CsdlProperty().setName("ID").setType(
					EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
			CsdlProperty status = new CsdlProperty().setName("Status").setType(
					EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty createTime = new CsdlProperty().setName("CreateTime")
					.setType(EdmPrimitiveTypeKind.Date.getFullQualifiedName());
			CsdlProperty isArchived = new CsdlProperty()
					.setName("IsArchived")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty isEnabled = new CsdlProperty()
					.setName("IsEnabled")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty isDeleted = new CsdlProperty()
					.setName("IsDeleted")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

			CsdlProperty patient = new CsdlProperty().setName("CTPatient")
					.setType(CT_PATIENT_FQN);
			CsdlProperty doctor = new CsdlProperty().setName("CTDoctor")
					.setType(CT_DOCTOR_FQN);
			CsdlProperty detail = new CsdlProperty().setName("CTDetail")
					.setType(CT_ORDER_DETAIL_FQN);

			List<CsdlNavigationProperty> navPropList = new ArrayList<CsdlNavigationProperty>();
			CsdlNavigationProperty convs = new CsdlNavigationProperty()
					.setName("OrderConvs").setType(ET_ORDER_CONV_FQN)
					.setCollection(true).setNullable(true);
			navPropList.add(convs);

			// create CsdlPropertyRef for Key element
			CsdlPropertyRef propertyRef = new CsdlPropertyRef();
			propertyRef.setName("ID");

			// configure EntityType
			CsdlEntityType entityType = new CsdlEntityType();
			entityType.setName(ES_ORDERS_NAME);
			entityType.setProperties(Arrays.asList(id, status, createTime,
					isArchived, isEnabled, isDeleted, patient, doctor, detail));
			entityType.setKey(Collections.singletonList(propertyRef));
			entityType.setNavigationProperties(navPropList);

			return entityType;
		}

		return null;
	}

	public static final CsdlEntityContainer getEntityContainer() {
		// create EntitySets
		List<CsdlEntitySet> entitySets = new ArrayList<CsdlEntitySet>();
		entitySets.addAll(AllEntitySetFQN);

		// create EntityContainer
		CsdlEntityContainer entityContainer = new CsdlEntityContainer();
		entityContainer.setName(CONTAINER_NAME);
		entityContainer.setEntitySets(entitySets);

		return entityContainer;
	}

	public static final CsdlEntitySet getEntitySet(
			FullQualifiedName entityContainer, String entitySetName) {
		if (entityContainer.equals(CONTAINER)) {
			if (entitySetName.equals(ES_DOCTORS_NAME)) {
				CsdlEntitySet entitySet = new CsdlEntitySet();
				entitySet.setName(ES_DOCTORS_NAME);
				entitySet.setType(ET_DOCTOR_FQN);
				return entitySet;
			} else if (entitySetName.equals(ES_PATIENTS_NAME)) {
				CsdlEntitySet entitySet = new CsdlEntitySet();
				entitySet.setName(ES_PATIENTS_NAME);
				entitySet.setType(ET_PATIENT_FQN);
				return entitySet;
			} else if (entitySetName.equals(ES_ORDER_DETAILS_NAME)) {
				CsdlEntitySet entitySet = new CsdlEntitySet();
				entitySet.setName(ES_ORDER_DETAILS_NAME);
				entitySet.setType(ET_ORDER_DETAIL_FQN);
				return entitySet;
			} else if (entitySetName.equals(ES_ORDER_CONVS_NAME)) {
				CsdlEntitySet entitySet = new CsdlEntitySet();
				entitySet.setName(ES_ORDER_CONVS_NAME);
				entitySet.setType(ET_ORDER_CONV_FQN);
				return entitySet;
			} else if (entitySetName.equals(ES_ORDERS_NAME)) {
				CsdlEntitySet entitySet = new CsdlEntitySet();
				entitySet.setName(ES_ORDERS_NAME);
				entitySet.setType(ET_ORDER_FQN);
				List<CsdlNavigationPropertyBinding> navigationPropertyBindings = new ArrayList<CsdlNavigationPropertyBinding>();
				CsdlNavigationPropertyBinding orderConvBd = new CsdlNavigationPropertyBinding();
				orderConvBd.setTarget("OrderConvs");
				orderConvBd.setPath("OrderConvs");
				navigationPropertyBindings.add(orderConvBd);
				entitySet
						.setNavigationPropertyBindings(navigationPropertyBindings);
				return entitySet;
			}
		}
		return null;
	}

	public static final String getEntitySetName(Entity entity) {
		String entitySetName = EntityToSetMap.get(entity.getType());
		if (entitySetName != null) {
			return entitySetName;
		}
		return entity.getType();
	}

	public static final CsdlComplexType getComplexType(
			final FullQualifiedName complexTypeName) {
		if (CT_DOCTOR_FQN.equals(complexTypeName)) {
			return new CsdlComplexType()
					.setName(CT_DOCTOR_FQN.getName())
					.setProperties(getEntityType(ET_DOCTOR_FQN).getProperties())
					.setOpenType(true);
		} else if (CT_PATIENT_FQN.equals(complexTypeName)) {
			return new CsdlComplexType()
					.setName(CT_PATIENT_FQN.getName())
					.setProperties(
							getEntityType(ET_PATIENT_FQN).getProperties())
					.setOpenType(true);
		} else if (CT_ORDER_DETAIL_FQN.equals(complexTypeName)) {
			return new CsdlComplexType()
					.setName(CT_ORDER_DETAIL_FQN.getName())
					.setProperties(
							getEntityType(ET_ORDER_DETAIL_FQN).getProperties())
					.setOpenType(true);
		}
		return null;
	}

}
