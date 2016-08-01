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
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmNavigationProperty;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.http.HttpHeader;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.ODataRequest;
import org.apache.olingo.server.api.ODataResponse;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.processor.EntityCollectionProcessor;
import org.apache.olingo.server.api.serializer.EntityCollectionSerializerOptions;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.api.serializer.SerializerException;
import org.apache.olingo.server.api.serializer.SerializerResult;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;
import org.apache.olingo.server.api.uri.UriResourceFunction;
import org.apache.olingo.server.api.uri.UriResourceNavigation;

import com.ehealth.mc.service.OverallService;
import com.ehealth.mc.service.util.EntityUtil;

public class McEntityCollectionProcessor implements EntityCollectionProcessor {

	private OData odata;
	private ServiceMetadata serviceMetadata;
	private OverallService overallService;

	public void init(OData odata, ServiceMetadata serviceMetadata) {
		this.odata = odata;
		this.serviceMetadata = serviceMetadata;
	}

	public void readEntityCollection(ODataRequest request,
			ODataResponse response, UriInfo uriInfo, ContentType responseFormat)
			throws ODataApplicationException, SerializerException {

		final UriResource firstResourceSegment = uriInfo.getUriResourceParts()
				.get(0);

		if (firstResourceSegment instanceof UriResourceEntitySet) {
			readEntityCollectionInternal(request, response, uriInfo,
					responseFormat);
		} else if (firstResourceSegment instanceof UriResourceFunction) {
			// readFunctionImportCollection(request, response, uriInfo,
			// responseFormat);
		} else {
			throw new ODataApplicationException("Not implemented",
					HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(),
					Locale.ENGLISH);
		}
	}

	public void readEntityCollectionInternal(ODataRequest request,
			ODataResponse response, UriInfo uriInfo, ContentType responseFormat)
			throws ODataApplicationException, SerializerException {

		// 1st we have retrieve the requested EntitySet from the uriInfo object
		// (representation of the parsed service URI)
		EntityCollection responseEntitySet = null;
		EntityCollectionSerializerOptions opts = null;
		EdmEntityType edmEntityType = null;

		List<UriResource> resourceParts = uriInfo.getUriResourceParts();
		int segmentCount = resourceParts.size();

		// Analyze the URI segments

		// 2nd: fetch the data from backend for this requested EntitySetName //
		if (segmentCount == 1) { // no navigation
			// UriResourceEntitySet uriResourceEntitySet =
			// (UriResourceEntitySet) resourceParts.get(0);
			// in our example, the first segment is the EntitySet

			UriResource uriResource = resourceParts.get(0);
			if (!(uriResource instanceof UriResourceEntitySet)) {
				throw new ODataApplicationException(
						"Only EntitySet is supported",
						HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(),
						Locale.ENGLISH);
			}

			UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet) uriResource;
			EdmEntitySet startEdmEntitySet = uriResourceEntitySet
					.getEntitySet();
			EdmEntitySet edmEntitySet = uriResourceEntitySet.getEntitySet();

			List<UriParameter> para = uriResourceEntitySet.getKeyPredicates();
			responseEntitySet = getData(edmEntitySet, uriInfo);

			edmEntityType = edmEntitySet.getEntityType();
			ContextURL contextUrl = ContextURL.with().entitySet(edmEntitySet)
					.build();

			final String id = request.getRawBaseUri() + "/"
					+ edmEntitySet.getName();
			opts = EntityCollectionSerializerOptions.with().id(id)
					.contextURL(contextUrl).build();

		} else if (segmentCount == 2) { // navigation

			UriResource firstResource = resourceParts.get(0);
			if (!(firstResource instanceof UriResourceEntitySet)) {
				throw new ODataApplicationException(
						"Only EntitySet is supported",
						HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(),
						Locale.ENGLISH);
			}
			UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet) firstResource;
			EdmEntitySet startEdmEntitySet = uriResourceEntitySet
					.getEntitySet();
			EdmEntitySet edmEntitySet = uriResourceEntitySet.getEntitySet();

			UriResourceEntitySet firstResourceEntitySet = (UriResourceEntitySet) firstResource;
			EdmEntitySet firstEdmEntitySet = firstResourceEntitySet
					.getEntitySet();
			List<UriParameter> firstKeyPredicates = firstResourceEntitySet
					.getKeyPredicates();
			Entity firstEntity = overallService.readEntityData(
					firstEdmEntitySet, firstKeyPredicates);

			UriResource secondSegment = resourceParts.get(1);
			if (firstEntity != null
					&& secondSegment instanceof UriResourceNavigation) {
				UriResourceNavigation uriResourceNavigation = (UriResourceNavigation) secondSegment;
				EdmNavigationProperty edmNavigationProperty = uriResourceNavigation
						.getProperty();
				responseEntitySet = EntityUtil.getRelatedEntityCollection(
						firstEntity, uriResourceNavigation);
				edmEntitySet = EntityUtil.getNavigationTargetEntitySet(
						startEdmEntitySet, edmNavigationProperty);
				edmEntityType = edmEntitySet.getEntityType();
				ContextURL contextUrl = ContextURL.with()
						.entitySet(edmEntitySet).build();
				final String id = request.getRawBaseUri() + "/"
						+ edmEntitySet.getName();
				opts = EntityCollectionSerializerOptions.with().id(id)
						.contextURL(contextUrl).build();
			}
		}

		ODataSerializer serializer = odata.createSerializer(responseFormat);
		SerializerResult serializedContent = serializer.entityCollection(
				serviceMetadata, edmEntityType, responseEntitySet, opts);

		// Finally: configure the response object: set the body, headers and
		// status code
		response.setContent(serializedContent.getContent());
		response.setStatusCode(HttpStatusCode.OK.getStatusCode());
		response.setHeader(HttpHeader.CONTENT_TYPE,
				responseFormat.toContentTypeString());
	}

	private EntityCollection getData(EdmEntitySet edmEntitySet, UriInfo uriInfo)
			throws ODataApplicationException, SerializerException {
		return overallService.findAll(edmEntitySet, uriInfo);
	}

	public void setOverallService(OverallService overallService) {
		this.overallService = overallService;
	}

}
