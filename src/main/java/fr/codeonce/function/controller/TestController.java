package fr.codeonce.function.controller;

import java.util.Date;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.codeonce.function.openFaas.OpenFaasInvoker;
import fr.codeonce.function.openFaas.OpenFaasRequest;

@Path("/ping")
public class TestController {

	@Inject
	OpenFaasInvoker openFaasInvoker;

	final Logger logger = LoggerFactory.getLogger(TestController.class);

	@GET
	public Date name() {
		logger.info("Request to ping the server");
		return new Date();
	}

	@POST
	public Object invoke(OpenFaasRequest req) throws Exception {
		return openFaasInvoker.invokeOpenFaas(req);

	}

}