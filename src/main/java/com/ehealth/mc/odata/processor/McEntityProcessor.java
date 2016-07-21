/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.ehealth.mc.odata.processor;

import java.util.List;
import java.util.Locale;

import org.apache.olingo.commons.api.data.ContextURL;
import org.apache.olingo.commons.api.data.ContextURL.Suffix;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmNavigationProperty;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.http.HttpHeader;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.ODataLibraryException;
import org.apache.olingo.server.api.ODataRequest;
import org.apache.olingo.server.api.ODataResponse;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.processor.EntityProcessor;
import org.apache.olingo.server.api.processor.MediaEntityProcessor;
import org.apache.olingo.server.api.serializer.EntitySerializerOptions;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.api.serializer.SerializerException;
import org.apache.olingo.server.api.serializer.SerializerResult;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;
import org.apache.olingo.server.api.uri.UriResourceFunction;
import org.apache.olingo.server.api.uri.UriResourceNavigation;
import org.apache.olingo.server.api.uri.queryoption.ExpandItem;
import org.apache.olingo.server.api.uri.queryoption.ExpandOption;
import org.apache.olingo.server.api.uri.queryoption.SelectOption;

import com.ehealth.mc.odata.util.Util;
import com.ehealth.mc.service.OverallService;

public class McEntityProcessor implements EntityProcessor, MediaEntityProcessor {

	private OData odata;
	private ServiceMetadata serviceMetadata;
	private OverallService overallService;

	public void init(OData odata, ServiceMetadata serviceMetadata) {
		this.odata = odata;
		this.serviceMetadata = serviceMetadata;
	}

	@Override
	public void readMediaEntity(ODataRequest request, ODataResponse response,
			UriInfo uriInfo, ContentType responseFormat)
			throws ODataApplicationException, ODataLibraryException {
		// TODO Auto-generated method stub

	}

	@Override
	public void createMediaEntity(ODataRequest request, ODataResponse response,
			UriInfo uriInfo, ContentType requestFormat,
			ContentType responseFormat) throws ODataApplicationException,
			ODataLibraryException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateMediaEntity(ODataRequest request, ODataResponse response,
			UriInfo uriInfo, ContentType requestFormat,
			ContentType responseFormat) throws ODataApplicationException,
			ODataLibraryException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteMediaEntity(ODataRequest request, ODataResponse response,
			UriInfo uriInfo) throws ODataApplicationException,
			ODataLibraryException {
		// TODO Auto-generated method stub

	}

	@Override
	public void readEntity(ODataRequest request, ODataResponse response,
			UriInfo uriInfo, ContentType responseFormat)
			throws ODataApplicationException, ODataLibraryException {
		UriResource uriResource = uriInfo.getUriResourceParts().get(0);

		if (uriResource instanceof UriResourceEntitySet) {
			readEntityInternal(request, response, uriInfo, responseFormat);
		} else if (uriResource instanceof UriResourceFunction) {
			// readFunctionImportInternal(request, response, uriInfo,
			// responseFormat);
		} else {
			throw new ODataApplicationException("Only EntitySet is supported",
					HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(),
					Locale.ENGLISH);
		}
	}

	@Override
	public void createEntity(ODataRequest request, ODataResponse response,
			UriInfo uriInfo, ContentType requestFormat,
			ContentType responseFormat) throws ODataApplicationException,
			ODataLibraryException {
		// TODO Auto-generated method stub
	}

	private void readFunctionImportInternal(final ODataRequest request,
			final ODataResponse response, final UriInfo uriInfo,
			final ContentType responseFormat) throws ODataApplicationException,
			SerializerException {

		// 1st step: Analyze the URI and fetch the entity returned by the
		// function import
		// Function Imports are always the first segment of the resource path
		final UriResource firstSegment = uriInfo.getUriResourceParts().get(0);

		if (!(firstSegment instanceof UriResourceFunction)) {
			throw new ODataApplicationException("Not implemented",
					HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(),
					Locale.ENGLISH);
		}

		final UriResourceFunction uriResourceFunction = (UriResourceFunction) firstSegment;
		final Entity entity = null;// overallService.readFunctionImportEntity(uriResourceFunction,
									// serviceMetadata);

		if (entity == null) {
			throw new ODataApplicationException("Nothing found.",
					HttpStatusCode.NOT_FOUND.getStatusCode(), Locale.ROOT);
		}

		// 2nd step: Serialize the response entity
		final EdmEntityType edmEntityType = (EdmEntityType) uriResourceFunction
				.getFunction().getReturnType().getType();
		final ContextURL contextURL = ContextURL.with().type(edmEntityType)
				.build();
		final EntitySerializerOptions opts = EntitySerializerOptions.with()
				.contextURL(contextURL).build();
		final ODataSerializer serializer = odata
				.createSerializer(responseFormat);
		final SerializerResult serializerResult = serializer.entity(
				serviceMetadata, edmEntityType, entity, opts);

		// 3rd configure the response object
		response.setContent(serializerResult.getContent());
		response.setStatusCode(HttpStatusCode.OK.getStatusCode());
		response.setHeader(HttpHeader.CONTENT_TYPE,
				responseFormat.toContentTypeString());
	}

	private void readEntityInternal(ODataRequest request,
			ODataResponse response, UriInfo uriInfo, ContentType responseFormat)
			throws ODataApplicationException, SerializerException {

		Entity responseEntity = null;
		// required for serialization of the response body
		EdmEntitySet responseEdmEntitySet = null;
		// we need this for building the contextUrl

		// 1st step: retrieve the requested Entity: can be "normal" read
		// operation, or navigation (to-one)
		List<UriResource> resourceParts = uriInfo.getUriResourceParts();
		int segmentCount = resourceParts.size();

		UriResource uriResource = resourceParts.get(0);
		// in our example, the first segment is the EntitySet
		if (!(uriResource instanceof UriResourceEntitySet)) {
			throw new ODataApplicationException("Only EntitySet is supported",
					HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(),
					Locale.ENGLISH);
		}

		UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet) uriResource;
		EdmEntitySet startEdmEntitySet = uriResourceEntitySet.getEntitySet();

		// Analyze the URI segments
		if (segmentCount == 1) { // no navigation
			responseEdmEntitySet = startEdmEntitySet; // since we have only one
														// segment

			// 2. step: retrieve the data from backend
			List<UriParameter> keyPredicates = uriResourceEntitySet
					.getKeyPredicates();
			responseEntity = overallService.readEntityData(startEdmEntitySet,
					keyPredicates);
		} else if (segmentCount == 2) { // navigation
			UriResource navSegment = resourceParts.get(1);
			// in our example we don't support more complex URIs
			if (navSegment instanceof UriResourceNavigation) {
				UriResourceNavigation uriResourceNavigation = (UriResourceNavigation) navSegment;
				EdmNavigationProperty edmNavigationProperty = uriResourceNavigation
						.getProperty();
				// contextURL displays the last segment
				responseEdmEntitySet = Util.getNavigationTargetEntitySet(
						startEdmEntitySet, edmNavigationProperty);

				// 2nd: fetch the data from backend.
				// e.g. for the URI: Products(1)/Category we have to find the
				// correct Category entity
				List<UriParameter> keyPredicates = uriResourceEntitySet
						.getKeyPredicates();
				// e.g. for Products(1)/Category we have to find first the
				// Products(1)
				Entity sourceEntity = overallService.readEntityData(
						startEdmEntitySet, keyPredicates);

				responseEntity = overallService.getRelatedEntity(sourceEntity,
						uriResourceNavigation);
			}
		} else {
			// this would be the case for e.g.
			// Products(1)/Category/Products(1)/Category
			throw new ODataApplicationException("Not supported",
					HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ROOT);
		}

		if (responseEntity == null) {
			// this is the case for e.g. DemoService.svc/Categories(4) or
			// DemoService.svc/Categories(3)/Products(999)
			throw new ODataApplicationException("Nothing found.",
					HttpStatusCode.NOT_FOUND.getStatusCode(), Locale.ROOT);
		}

		// 3. apply system query options

		// handle $select
		SelectOption selectOption = uriInfo.getSelectOption();
		// in our example, we don't have performance issues, so we can rely upon
		// the handling in the Olingo lib
		// nothing else to be done

		// handle $expand
		ExpandOption expandOption = uriInfo.getExpandOption();
		// Nested system query options are not implemented
		validateNestedExpxandSystemQueryOptions(expandOption);

		// 4. serialize
		EdmEntityType edmEntityType = responseEdmEntitySet.getEntityType();
		// we need the property names of the $select, in order to build the
		// context URL
		String selectList = odata.createUriHelper().buildContextURLSelectList(
				edmEntityType, expandOption, selectOption);
		ContextURL contextUrl = ContextURL.with()
				.entitySet(responseEdmEntitySet).selectList(selectList)
				.suffix(Suffix.ENTITY).build();

		// make sure that $expand and $select are considered by the serializer
		// adding the selectOption to the serializerOpts will actually tell the
		// lib to do the job
		EntitySerializerOptions opts = EntitySerializerOptions.with()
				.contextURL(contextUrl).select(selectOption)
				.expand(expandOption).build();

		ODataSerializer serializer = this.odata
				.createSerializer(responseFormat);
		SerializerResult serializerResult = serializer.entity(serviceMetadata,
				edmEntityType, responseEntity, opts);

		// 5. configure the response object
		response.setContent(serializerResult.getContent());
		response.setStatusCode(HttpStatusCode.OK.getStatusCode());
		response.setHeader(HttpHeader.CONTENT_TYPE,
				responseFormat.toContentTypeString());
	}

	private void validateNestedExpxandSystemQueryOptions(
			final ExpandOption expandOption) throws ODataApplicationException {
		if (expandOption == null) {
			return;
		}

		for (final ExpandItem item : expandOption.getExpandItems()) {
			if (item.getCountOption() != null || item.getFilterOption() != null
					|| item.getLevelsOption() != null
					|| item.getOrderByOption() != null
					|| item.getSearchOption() != null
					|| item.getSelectOption() != null
					|| item.getSkipOption() != null
					|| item.getTopOption() != null) {

				throw new ODataApplicationException(
						"Nested expand system query options are not implemented",
						HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(),
						Locale.ENGLISH);
			}
		}
	}

	@Override
	public void updateEntity(ODataRequest request, ODataResponse response,
			UriInfo uriInfo, ContentType requestFormat,
			ContentType responseFormat) throws ODataApplicationException,
			ODataLibraryException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteEntity(ODataRequest request, ODataResponse response,
			UriInfo uriInfo) throws ODataApplicationException,
			ODataLibraryException {
		// TODO Auto-generated method stub

	}

	public void setOverallService(OverallService overallService) {
		this.overallService = overallService;
	}

}
