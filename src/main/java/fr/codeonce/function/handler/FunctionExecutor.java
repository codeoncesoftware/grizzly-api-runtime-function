package fr.codeonce.function.handler;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.graalvm.polyglot.Engine;

import fr.codeonce.function.awsLambda.AWSLambdaRequest;
import fr.codeonce.function.awsLambda.AwsLambdaInvoker;
import fr.codeonce.function.dto.FunctionInvokeRequest;
import fr.codeonce.function.dto.FunctionsExecuteRequest;
import fr.codeonce.function.dto.Response;
import fr.codeonce.function.openFaas.OpenFaasInvoker;
import fr.codeonce.function.openFaas.OpenFaasRequest;
import fr.codeonce.function.service.JavaCompilerServiceV2;
import fr.codeonce.function.service.JavaScriptCompilerService;

@ApplicationScoped
public class FunctionExecutor {

	@Inject
	JavaScriptCompilerService javaScriptCompilerService;

	@Inject
	JavaCompilerServiceV2 javaCompilerService;

	// engine to share the cache between contexts
	Engine engine = Engine.newBuilder().build();

	@Inject
	AwsLambdaInvoker awsLambdaInvoker;

	@Inject
	OpenFaasInvoker openFaasInvoker;

	/**
	 * this method used to select the function executor based on received language
	 * 
	 * @param language This is the function's language
	 * @param function This is the function to be executed
	 * @param argument This is the parameter of the function
	 * @param name     This is the function's name
	 * @return Response
	 *
	 */
	public Response executeFunction(FunctionsExecuteRequest request) {

		switch (request.getFunction().getLanguage()) {
		case "javascript":
			return javaScriptCompilerService.execute(request.getFunction(), request.getJsonRequestModel(),
					request.getJsonPersistanceModel(), null, 0, request.getExecutionType(), request.getMongoURI(),
					request.getCollectionName(), request.getDbName(), request.getProvider(), request.getLogId(),
					engine);
		case "java":
			return javaCompilerService.execute(request.getFunction(), request.getJsonRequestModel(),
					request.getJsonPersistanceModel(), request.getPojoRequestModel(),
					request.getLastUpdatRequestModel(), request.getExecutionType(), request.getMongoURI(),
					request.getCollectionName(), request.getDbName(), request.getProvider(), request.getLogId(), null);
		default:
			return null;
		}

	}

	public Response invokeFunction(FunctionInvokeRequest req) throws Exception {

		if (req instanceof AWSLambdaRequest) {
			System.out.println("AWD Lambda !");
			return (Response) awsLambdaInvoker.invokeAWSLambda((AWSLambdaRequest) req);
		} else {
			System.out.println("Open Faas !");
			System.out.println("Function open faas uri" + ((OpenFaasRequest) req).getUri());

			return (Response) openFaasInvoker.invokeOpenFaas((OpenFaasRequest) req);

		}

	}

}
