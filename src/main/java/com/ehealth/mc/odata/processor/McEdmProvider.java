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

import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlAbstractEdmProvider;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainer;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainerInfo;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlSchema;

import com.ehealth.mc.service.util.McEdmUtil;

/**
 * this class is supposed to declare the metadata of the OData service it is
 * invoked by the Olingo framework e.g. when the metadata document of the
 * service is invoked e.g.
 * http://localhost:8080/ExampleService1/ExampleService1.svc/$metadata
 */
public class McEdmProvider extends CsdlAbstractEdmProvider {

	@Override
	public List<CsdlSchema> getSchemas() {
		return McEdmUtil.getSchemas();
	}

	@Override
	public CsdlEntityType getEntityType(FullQualifiedName entityTypeName) {
		return McEdmUtil.getEntityType(entityTypeName);
	}

	@Override
	public CsdlEntitySet getEntitySet(FullQualifiedName entityContainer,
			String entitySetName) {
		return McEdmUtil.getEntitySet(entityContainer, entitySetName);
	}

	@Override
	public CsdlEntityContainer getEntityContainer() {
		return McEdmUtil.getEntityContainer();
	}

	@Override
	public CsdlEntityContainerInfo getEntityContainerInfo(
			FullQualifiedName entityContainerName) {
		return McEdmUtil.getEntityContainerInfo(entityContainerName);
	}
}
