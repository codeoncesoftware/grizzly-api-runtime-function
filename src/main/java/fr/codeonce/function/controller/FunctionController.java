package fr.codeonce.function.controller;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.codeonce.function.dto.FunctionInvokeRequest;
import fr.codeonce.function.dto.FunctionsExecuteRequest;
import fr.codeonce.function.dto.Response;
import fr.codeonce.function.handler.FunctionExecutor;
import fr.codeonce.function.openFaas.OpenFaasRequest;

@Path("/function")
public class FunctionController {

	final Logger logger = LoggerFactory.getLogger(FunctionController.class);

	@Inject
	FunctionExecutor functionExecutor;

	@POST
	@Path("execute")
	@Produces(MediaType.APPLICATION_JSON)
	public Response execute(FunctionsExecuteRequest f) {
		logger.info("Request to execute {} function with ID: {}  with execution type: {}",
				f.getFunction().getLanguage(), f.getFunction().getId(), f.getExecutionType());
		return this.functionExecutor.executeFunction(f);

	}

	@POST
	@Path("invoke")
	@Produces(MediaType.APPLICATION_JSON)
	public Response invoke(fr.codeonce.function.awsLambda.AWSLambdaRequest f) throws Exception {
		logger.info("Request to invoke {} function with ID: {}  deployed on : {}", f.getFunctionName(), f.getType());
		return this.functionExecutor.invokeFunction(f);

	}
	

	@POST
	@Path("/openFaas")
	@Produces(MediaType.APPLICATION_JSON)
	public Response invokeOpenFaas(OpenFaasRequest f) throws Exception {
		logger.info("Request to invoke OpenFaas Function");
		return this.functionExecutor.invokeFunction(f);

	}

}