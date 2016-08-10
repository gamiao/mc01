package com.ehealth.mc.service;

import java.util.List;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.ODataRequest;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriParameter;

public interface OverallService {

	Entity readEntityData(EdmEntitySet edmEntitySet,
			List<UriParameter> keyParams) throws ODataApplicationException;

	EntityCollection findAll(EdmEntitySet edmEntitySet, UriInfo uriInfo)
			throws ODataApplicationException;

	Entity createEntityData(ODataRequest request, EdmEntitySet edmEntitySet,
			Entity requestEntity, OData odata, ServiceMetadata edm)
			throws ODataApplicationException;

	void updateEntityData(ODataRequest request, List<UriParameter> keyParams,
			EdmEntitySet edmEntitySet, Entity requestEntity, OData odata,
			ServiceMetadata edm) throws ODataApplicationException;

	String updateEntityAfterFileUploaded(String entityType, String entityID,
			String method, String fileName);

	Entity createCascatedEntityData(Entity firstEntity, ODataRequest request,
			EdmEntitySet edmEntitySet, Entity requestEntity, OData odata,
			ServiceMetadata serviceMetadata) throws ODataApplicationException;

}
