package com.ehealth.mc.service.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainer;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainerInfo;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
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
	public static final String ET_ORDER_NAME = "Order";
	public static final FullQualifiedName ET_ORDER_FQN = new FullQualifiedName(
			NAMESPACE, ET_ORDER_NAME);

	// Entity Set Names
	public static final String ES_DOCTORS_NAME = "Doctors";
	public static final String ES_PATIENTS_NAME = "Patients";
	public static final String ES_ORDERS_NAME = "Orders";

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
		entityTypes.add(getEntityType(ET_ORDER_FQN));
		schema.setEntityTypes(entityTypes);

		// add EntityContainer
		schema.setEntityContainer(getEntityContainer());

		// finally
		List<CsdlSchema> schemas = new ArrayList<CsdlSchema>();
		schemas.add(schema);

		return schemas;
	}

	public static final CsdlEntityType getEntityType(
			FullQualifiedName entityTypeName) {
		// this method is called for one of the EntityTypes that are configured
		// in the Schema
		if (entityTypeName.equals(ET_DOCTOR_FQN)) {

			// create EntityType properties
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

			// create EntityType properties
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
			entityType.setName(ES_DOCTORS_NAME);
			entityType.setProperties(Arrays.asList(id, name, gender, avatar,
					address, mobile, birthday));
			entityType.setKey(Collections.singletonList(propertyRef));

			return entityType;
		}

		return null;
	}

	public static final CsdlEntityContainer getEntityContainer() {
		// create EntitySets
		List<CsdlEntitySet> entitySets = new ArrayList<CsdlEntitySet>();
		entitySets.add(getEntitySet(CONTAINER, ES_DOCTORS_NAME));
		entitySets.add(getEntitySet(CONTAINER, ES_PATIENTS_NAME));
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
			} else if (entitySetName.equals(ES_ORDERS_NAME)) {
				CsdlEntitySet entitySet = new CsdlEntitySet();
				entitySet.setName(ES_ORDERS_NAME);
				entitySet.setType(ET_ORDER_FQN);
				return entitySet;
			}
		}
		return null;
	}

}
