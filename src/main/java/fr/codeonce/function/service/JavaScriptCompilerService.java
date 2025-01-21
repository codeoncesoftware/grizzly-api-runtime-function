package fr.codeonce.function.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.codeonce.function.dto.Response;
import fr.codeonce.function.dto.RuntimeResourceFunction;
import fr.codeonce.function.handler.JavaScriptUtils;
import fr.codeonce.function.predefined.javascript.function.errorFunction.ErrorFunction;
import fr.codeonce.function.predefined.javascript.function.executeQuery.ExecuteQueryFunction;
import fr.codeonce.function.predefined.javascript.function.httpRequest.HttpClientFunction;
import fr.codeonce.function.predefined.javascript.function.jwt.ParseJwtTokenFunction;
import fr.codeonce.function.predefined.javascript.function.logsFunction.LoggerFunction;
import fr.codeonce.function.predefined.javascript.function.saveEntity.ExecuteSaveUpdateFunction;
import fr.codeonce.function.utils.PolyglotUtils;

@ApplicationScoped
public class JavaScriptCompilerService implements RuntimeCompiler {

	final Logger logger = LoggerFactory.getLogger(JavaScriptCompilerService.class);

	@Inject
	private ExecuteQueryFunction findFunction;

	@Inject
	private ErrorFunction errorFunction;

	@Inject
	private LoggerFunction loggerFunction;

	@Inject
	private ExecuteSaveUpdateFunction saveFunction;

	@Inject
	private ParseJwtTokenFunction parseJwtTokenFunction;

	@Inject
	private HttpClientFunction httpClient;

	@Override
	public Response execute(RuntimeResourceFunction function, String requestObjectString,
			String persistanceObjectString, String pojoObjectString, float LastUpdateRequestModel, String executiontype,
			String mongoURI, String collectionName, String dbName, String provider, String logId, Engine engine) {

		// new context for each new execution using cache engine
		Context ctx = Context.newBuilder("js").allowExperimentalOptions(true).option("js.console", "false")
				.engine(engine).build();
		// prepare predefined functions to be used within the function

		evalPredefinedFunction(function, logId, mongoURI, collectionName, dbName, provider, ctx);

		// executor service to execute script asynchronously in a new thread
		Future<?> futureResult;
		try {
			futureResult = executeJsScript(function.getFunction(), executiontype, requestObjectString,
					persistanceObjectString, ctx);

		} catch (Exception e2) {
			return new Response.ResponseBuilder().httpCode(400)
					.response("The function with name '" + function.getName() + "' is not executable !")
					.type("exception").build();
		}

		try {
			// if the function does not end in 10 sec stop script
			Object result = futureResult.get(10, TimeUnit.SECONDS);

			logger.info("{} function with ID: {}  has been executed successfully", "Javascript", function.getId());

			if (ctx.getBindings("js").getMember("isException").asBoolean() == true) {// in case there is an exception

				if (!((Value) result).getMember("response").isString()) {
					return new Response.ResponseBuilder().httpCode(400)
							.response("First Argument of _throwException() Must Be A String !").type("exception")
							.build();
				}
				if (!((Value) result).getMember("httpCode").isNumber()
						|| ((Value) result).getMember("httpCode").asInt() < 100
						|| ((Value) result).getMember("httpCode").asInt() > 599) {

					return new Response.ResponseBuilder().httpCode(400)
							.response("Http Code of _throwException() must be Integer between 100-599  !")
							.type("exception").build();

				}

				// thrown by the client
				return new Response.ResponseBuilder().type(((Value) result).getMember("type").asString())
						.response(((Value) result).getMember("response").asString())
						.httpCode(((Value) result).getMember("httpCode").asInt()).build();
			}

			// parse the graalvm Value to it's real type
			result = PolyglotUtils.checkReturnType((Value) result);
			// return the response in case there is no problem
			return serializeResponse(result, ctx);

		} catch (ClassCastException e) {

			return new Response.ResponseBuilder().httpCode(400)
					.response("Http Code of _throwException() must be Integer between 100-599  !").type("exception")
					.build();
		}

		catch (TimeoutException e) {// in case that the function taking too long
			// stop the function to prevent the infinite loops
			futureResult.cancel(true);
			logger.error("{} function with ID: {}  has been interrupted", "JavaScript", function.getId());
			return new Response.ResponseBuilder().type("security")
					.response("The Function is interupted for taking too long").build();

		} catch (ExecutionException e) {// in case of thrown exception by client, or a problem
			if (e.getCause() != null && e.getCause().getMessage() != null) {
				return new Response.ResponseBuilder().type("exception")
						.response(e.getCause().getMessage().replaceAll("\"", "'")).build();
			}
			return new Response.ResponseBuilder().type("exception").response(e.getMessage().replaceAll("\"", "'"))
					.build();

		} catch (InterruptedException e) {
			logger.error("{} function with ID: {} cannot be executed", "JavaScript", function.getId());
		}
		return null; // return null if none of the case

	}

	private void evalPredefinedFunction(RuntimeResourceFunction function, String logId, String mongoURI,
			String collectionName, String dbName, String provider, Context ctx) {

		loggerFunction.evalLoggerFunction(logId, function.getProjectId(),
				function.getName() + "-" + function.getVersion(), ctx);
		errorFunction.errorJs(ctx);
		httpClient.evalHttpClient(ctx);
		parseJwtTokenFunction.evalJwtDecoderAndVerifier(ctx);
		findFunction.evalMongoExecuteQueryFunction(mongoURI, collectionName, dbName, provider, ctx);
		saveFunction.evalMongoSaveUpdateFunction(mongoURI, collectionName, dbName, provider, ctx);
	}

	// execute the script
	private Future<?> executeJsScript(String function, String executiontype, String requestObjectString,
			String persistanceObjectString, Context ctx) throws Exception {

		final ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<?> futureResult = null;
		Value functionToExecute = ctx.eval("js", function);
		;
		if (executiontype.equalsIgnoreCase("in")) {

			if (!functionToExecute.canExecute())
				throw new Exception();

			futureResult = executor.submit(
					() -> ctx.eval("js", function).execute(JavaScriptUtils.jsonToJsObject(requestObjectString, ctx)));

		} else {
			if (!functionToExecute.canExecute())
				throw new Exception();

			futureResult = executor.submit(
					() -> ctx.eval("js", function).execute(JavaScriptUtils.jsonToJsObject(requestObjectString, ctx),
							JavaScriptUtils.jsonToJsObject(persistanceObjectString, ctx)));
		}

		return futureResult;

	}

	// prepare the Response to be returned to the client
	private Response serializeResponse(Object result, Context ctx) {

		if (!(result instanceof String) && !(result instanceof Boolean) && !(result instanceof Number)) {
			return new Response.ResponseBuilder()
					.response(ctx.eval("js", "stringify=(p)=>{ return JSON.stringify(p); }").execute(result).asString())
					.type("valid").build();
		} else {
			return new Response.ResponseBuilder().response(result).type("valid").build();
		}

	}

}
