package com.ehealth.mc.service;

import java.util.List;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.ODataRequest;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.uri.UriParameter;

public interface OverallService {

	public Entity readEntityData(EdmEntitySet edmEntitySet,
			List<UriParameter> keyParams) throws ODataApplicationException;

	public EntityCollection findAll(EdmEntitySet edmEntitySet)
			throws ODataApplicationException;

	public Entity createEntityData(ODataRequest request,
			EdmEntitySet edmEntitySet, Entity requestEntity, OData odata,
			ServiceMetadata edm) throws ODataApplicationException;

}
