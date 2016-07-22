package com.ehealth.mc.service.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlComplexType;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainer;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainerInfo;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlNavigationProperty;
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

	// Entity Set Names
	public static final String ES_DOCTORS_NAME = "Doctors";
	public static final String ES_PATIENTS_NAME = "Patients";
	public static final String ES_ORDERS_NAME = "Orders";
	public static final String ES_ORDER_DETAILS_NAME = "OrderDetails";

	// Complex Type Names
	public static final FullQualifiedName CT_DOCTOR_NAME = new FullQualifiedName(
			NAMESPACE, "CTDoctor");
	public static final FullQualifiedName CT_PATIENT_NAME = new FullQualifiedName(
			NAMESPACE, "CTPatient");
	public static final FullQualifiedName CT_ORDER_DETAIL_NAME = new FullQualifiedName(
			NAMESPACE, "CTOrderDetail");

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
		entityTypes.add(getEntityType(ET_DOCTOR_FQN));
		entityTypes.add(getEntityType(ET_PATIENT_FQN));
		entityTypes.add(getEntityType(ET_ORDER_DETAIL_FQN));
		entityTypes.add(getEntityType(ET_ORDER_FQN));
		schema.setEntityTypes(entityTypes);

		// ComplexTypes
		List<CsdlComplexType> complexTypes = new ArrayList<CsdlComplexType>();
		complexTypes.add(getComplexType(CT_DOCTOR_NAME));
		complexTypes.add(getComplexType(CT_PATIENT_NAME));
		complexTypes.add(getComplexType(CT_ORDER_DETAIL_NAME));
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
			CsdlProperty name = new CsdlProperty().setName("Name").setType(
					EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty gender = new CsdlProperty().setName("Gender").setType(
					EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty avatar = new CsdlProperty().setName("Avatar").setType(
					EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty address = new CsdlProperty()
					.setName("Address")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty mobile = new CsdlProperty().setName("Mobile").setType(
					EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty birthday = new CsdlProperty()
					.setName("Birthday")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty medicalLevel = new CsdlProperty().setName(
					"MedicalLevel").setType(
					EdmPrimitiveTypeKind.String.getFullQualifiedName());

			// create CsdlPropertyRef for Key element
			CsdlPropertyRef propertyRef = new CsdlPropertyRef();
			propertyRef.setName("ID");

			// configure EntityType
			CsdlEntityType entityType = new CsdlEntityType();
			entityType.setName(ES_DOCTORS_NAME);
			entityType.setProperties(Arrays.asList(id, name, gender, avatar,
					address, mobile, birthday, medicalLevel));
			entityType.setKey(Collections.singletonList(propertyRef));

			return entityType;
		} else if (entityTypeName.equals(ET_PATIENT_FQN)) {
			CsdlProperty id = new CsdlProperty().setName("ID").setType(
					EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
			CsdlProperty name = new CsdlProperty().setName("Name").setType(
					EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty gender = new CsdlProperty().setName("Gender").setType(
					EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty avatar = new CsdlProperty().setName("Avatar").setType(
					EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty address = new CsdlProperty()
					.setName("Address")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty mobile = new CsdlProperty().setName("Mobile").setType(
					EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty birthday = new CsdlProperty()
					.setName("Birthday")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

			// create CsdlPropertyRef for Key element
			CsdlPropertyRef propertyRef = new CsdlPropertyRef();
			propertyRef.setName("ID");

			// configure EntityType
			CsdlEntityType entityType = new CsdlEntityType();
			entityType.setName(ES_PATIENTS_NAME);
			entityType.setProperties(Arrays.asList(id, name, gender, avatar,
					address, mobile, birthday));
			entityType.setKey(Collections.singletonList(propertyRef));

			return entityType;
		} else if (entityTypeName.equals(ET_ORDER_DETAIL_FQN)) {
			CsdlProperty id = new CsdlProperty().setName("ID").setType(
					EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
			CsdlProperty type = new CsdlProperty().setName("Type").setType(
					EdmPrimitiveTypeKind.String.getFullQualifiedName());
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
			entityType.setProperties(Arrays.asList(id, type, title,
					description, pictures));
			entityType.setKey(Collections.singletonList(propertyRef));

			return entityType;
		} else if (entityTypeName.equals(ET_ORDER_FQN)) {
			// create EntityType properties
			CsdlProperty id = new CsdlProperty().setName("ID").setType(
					EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
			CsdlProperty status = new CsdlProperty().setName("Status").setType(
					EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty isArchived = new CsdlProperty()
					.setName("IsArchived")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty isEnabled = new CsdlProperty()
					.setName("IsEnabled")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty isDeleted = new CsdlProperty()
					.setName("IsDeleted")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

			//
			CsdlProperty patient = new CsdlProperty().setName("Patient")
					.setType(CT_PATIENT_NAME);
			CsdlProperty doctor = new CsdlProperty().setName("Doctor").setType(
					CT_DOCTOR_NAME);
			CsdlProperty detail = new CsdlProperty().setName("Detail").setType(
					CT_ORDER_DETAIL_NAME);

			List<CsdlNavigationProperty> navPropList = new ArrayList<CsdlNavigationProperty>();

			// navigation property: many-to-one, null not allowed (product must
			// have a category)
			/*
			 * CsdlNavigationProperty patientNavProp = new
			 * CsdlNavigationProperty()
			 * .setName("Patient").setType(ET_PATIENT_FQN) .setNullable(true);
			 * navPropList.add(patientNavProp); CsdlNavigationProperty
			 * doctorNavProp = new CsdlNavigationProperty()
			 * .setName("Doctor").setType(ET_DOCTOR_FQN).setNullable(true);
			 * navPropList.add(doctorNavProp); CsdlNavigationProperty
			 * orderDetailNavProp = new CsdlNavigationProperty()
			 * .setName("Detail").setType(ET_ORDER_DETAIL_FQN)
			 * .setNullable(true); navPropList.add(orderDetailNavProp);
			 */

			// create CsdlPropertyRef for Key element
			CsdlPropertyRef propertyRef = new CsdlPropertyRef();
			propertyRef.setName("ID");

			// configure EntityType
			CsdlEntityType entityType = new CsdlEntityType();
			entityType.setName(ES_ORDERS_NAME);
			entityType.setProperties(Arrays.asList(id, status, isArchived,
					isEnabled, isDeleted, patient, doctor, detail));
			entityType.setKey(Collections.singletonList(propertyRef));
			entityType.setNavigationProperties(navPropList);

			return entityType;
		}

		return null;
	}

	public static final CsdlEntityContainer getEntityContainer() {
		// create EntitySets
		List<CsdlEntitySet> entitySets = new ArrayList<CsdlEntitySet>();
		entitySets.add(getEntitySet(CONTAINER, ES_DOCTORS_NAME));
		entitySets.add(getEntitySet(CONTAINER, ES_PATIENTS_NAME));
		entitySets.add(getEntitySet(CONTAINER, ES_ORDER_DETAILS_NAME));
		entitySets.add(getEntitySet(CONTAINER, ES_ORDERS_NAME));

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
			} else if (entitySetName.equals(ES_ORDERS_NAME)) {
				CsdlEntitySet entitySet = new CsdlEntitySet();
				entitySet.setName(ES_ORDERS_NAME);
				entitySet.setType(ET_ORDER_FQN);
				return entitySet;
			}
		}
		return null;
	}

	public static final CsdlComplexType getComplexType(
			final FullQualifiedName complexTypeName) {
		if (CT_DOCTOR_NAME.equals(complexTypeName)) {
			return new CsdlComplexType()
					.setName(CT_DOCTOR_NAME.getName())
					.setProperties(getEntityType(ET_DOCTOR_FQN).getProperties())
					.setOpenType(true);
		} else if (CT_PATIENT_NAME.equals(complexTypeName)) {
			return new CsdlComplexType()
					.setName(CT_PATIENT_NAME.getName())
					.setProperties(
							getEntityType(ET_PATIENT_FQN).getProperties())
					.setOpenType(true);
		} else if (CT_ORDER_DETAIL_NAME.equals(complexTypeName)) {
			return new CsdlComplexType()
					.setName(CT_ORDER_DETAIL_NAME.getName())
					.setProperties(
							getEntityType(ET_ORDER_DETAIL_FQN).getProperties())
					.setOpenType(true);
		}
		return null;
	}

}
