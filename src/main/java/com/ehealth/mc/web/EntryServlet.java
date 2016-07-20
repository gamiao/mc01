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
package com.ehealth.mc.web;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.olingo.commons.api.edmx.EdmxReference;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataHttpHandler;
import org.apache.olingo.server.api.ServiceMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ehealth.mc.dao.DoctorDAO;
import com.ehealth.mc.odata.processor.McEdmProvider;
import com.ehealth.mc.odata.processor.McEntityCollectionProcessor;
import com.ehealth.mc.service.DoctorService;

/**
 * This class represents a standard HttpServlet implementation. It is used as
 * main entry point for the web application that carries the OData service. The
 * implementation of this HttpServlet simply delegates the user requests to the
 * ODataHttpHandler
 */
public class EntryServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory
			.getLogger(EntryServlet.class);

	@Override
	protected void service(final HttpServletRequest req,
			final HttpServletResponse resp) throws ServletException,
			IOException {

		try {

			fixHeaders(resp);
			OData odata = OData.newInstance();
			ServiceMetadata edm = odata.createServiceMetadata(
					new McEdmProvider(), new ArrayList<EdmxReference>());
			ODataHttpHandler handler = odata.createHandler(edm);
			McEntityCollectionProcessor collectionProcessor = new McEntityCollectionProcessor();

			ApplicationContext ac = WebApplicationContextUtils
					.getWebApplicationContext(req.getSession()
							.getServletContext());
			DoctorDAO doctorDAO = (DoctorDAO) ac.getBean("doctorDAO");
			DoctorService doctorService = (DoctorService) ac
					.getBean("doctorService");
			collectionProcessor.setDoctorService(doctorService);

			handler.register(collectionProcessor);

			// let the handler do the work
			handler.process(req, resp);

		} catch (RuntimeException e) {
			LOG.error("Server Error occurred in ExampleServlet", e);
			throw new ServletException(e);
		}
	}

	private void fixHeaders(HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods",
				"GET, PUT, POST, OPTIONS, DELETE");
		response.addHeader("Access-Control-Allow-Headers", "Content-Type");
		response.addHeader("Access-Control-Max-Age", "86400");
	}
}
