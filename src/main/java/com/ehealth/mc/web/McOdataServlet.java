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

import com.ehealth.mc.odata.processor.McEdmProvider;
import com.ehealth.mc.odata.processor.McEntityCollectionProcessor;
import com.ehealth.mc.odata.processor.McEntityProcessor;
import com.ehealth.mc.service.OverallService;

public class McOdataServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory
			.getLogger(McOdataServlet.class);

	@Override
	protected void service(final HttpServletRequest req,
			final HttpServletResponse resp) throws ServletException,
			IOException {

		try {
			fixHeaders(resp);

			OData odata = OData.newInstance();
			ServiceMetadata edm = odata.createServiceMetadata(
					new McEdmProvider(), new ArrayList<EdmxReference>());
			ApplicationContext ac = WebApplicationContextUtils
					.getWebApplicationContext(req.getSession()
							.getServletContext());
			OverallService overallService = (OverallService) ac
					.getBean("overallService");
			
			ODataHttpHandler handler = odata.createHandler(edm);
			McEntityProcessor entityProcessor = new McEntityProcessor();
			McEntityCollectionProcessor collectionProcessor = new McEntityCollectionProcessor();

			entityProcessor.setOverallService(overallService);
			collectionProcessor.setOverallService(overallService);

			handler.register(collectionProcessor);
			handler.register(entityProcessor);

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
