package com.ehealth.mc.service;

import java.util.List;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResourceNavigation;

public interface OverallService {

	public Entity readEntityData(EdmEntitySet edmEntitySet,
			List<UriParameter> keyParams) throws ODataApplicationException;

	public Entity getRelatedEntity(Entity entity,
			UriResourceNavigation navigationResource)
			throws ODataApplicationException;

	public EntityCollection findAll(EdmEntitySet edmEntitySet)
			throws ODataApplicationException;

}
