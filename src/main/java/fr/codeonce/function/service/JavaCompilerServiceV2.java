package fr.codeonce.function.service;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.enterprise.context.ApplicationScoped;

import org.graalvm.polyglot.Engine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.google.gson.Gson;

import fr.codeonce.function.dto.Response;
import fr.codeonce.function.dto.RuntimeResourceFunction;
import fr.codeonce.function.security.FunctionThread;
import net.openhft.compiler.CompilerUtils;

@ApplicationScoped

public class JavaCompilerServiceV2 implements RuntimeCompiler {

	final private Logger logger = LoggerFactory.getLogger(JavaCompilerServiceV2.class);

	@Override
	public Response execute(RuntimeResourceFunction function, String requestObjectString,
			String persistanceObjectString, String pojoObject, float lastUpdateRequestModel, String executiontype,
			String mongoURI, String collectionName, String dbName, String provider, String logId, Engine engine) {
		// final result variable, first colon for the result and second for exceptions
		final Object[] result = new Object[2];
		String[] exception = new String[] { "" };
		final int[] httpCode = new int[] { -1 };

		try {
			// required variables
			final Class<?> mainFunction;
			final Class<?> requestClass;
			final Class<?> persistanceClass;
			Object persistanceObject = null;
			Object requestObject = null;
			final Object insertedBody;
			final Object insertedInput;
			Method functionMethod = null;
			Object classInstance;
			// build the package to be unique based on lastupdate and function's id and
			// resource
			// String packageName = "grizzlyApi." + "p" + function.getId() +
			// function.getLastUpdate().getTime() / 1000;
			String requestPacakgeName = "request" + (int) lastUpdateRequestModel
					+ function.getLastUpdate().getTime() / 1000;
			// compile the inserted model and cache it
			requestClass = CompilerUtils.CACHED_COMPILER.loadFromJava(requestPacakgeName + ".Request",
					"package " + requestPacakgeName + ";\n" + "import java.util.*;\r\n" + "" + pojoObject);

			persistanceClass = CompilerUtils.CACHED_COMPILER.loadFromJava(
					requestPacakgeName + "." + function.getModelName(),
					"package " + requestPacakgeName + ";\n" + function.getModel());
			// compile the inserted main class and cache it
			mainFunction = CompilerUtils.CACHED_COMPILER.loadFromJava(
					requestPacakgeName + "." + function.getClassName(),
					"package " + requestPacakgeName + ";\n" + "import grizzlyApi.*;\n" + function.getFunction());
			classInstance = mainFunction.newInstance();

			// choose which method to fetch based on type of execution
			ObjectMapper objectMapper = new ObjectMapper();
			if (executiontype.equalsIgnoreCase("in")) {

				functionMethod = mainFunction.getMethod(function.getMethodName(), requestClass);

				requestObject = objectMapper.readValue(requestObjectString, requestClass);

			} else {// if out function
				if (persistanceObjectString.startsWith("[")) {
					functionMethod = mainFunction.getMethod(function.getMethodName(), requestClass,
							Array.newInstance(persistanceClass, 10).getClass());
					persistanceObject = objectMapper.readValue(persistanceObjectString,
							Array.newInstance(persistanceClass, 0).getClass());
				} else {
					functionMethod = mainFunction.getMethod(function.getMethodName(), requestClass, persistanceClass);
					persistanceObject = objectMapper.readValue(persistanceObjectString, persistanceClass);

				}
				requestObject = objectMapper.readValue(requestObjectString, requestClass);
			}

			// this variable to ensure thread safe variable to our intern thread
			Method method2 = functionMethod;
			insertedBody = requestObject;
			insertedInput = persistanceObject;

			// execute the function inside a new thread for security purpose
			FunctionThread functionThread = new FunctionThread(new Runnable() {

				@Override
				public void run() {

					// choose which function to invoke based on type of the execution
					try {
						if (executiontype.equalsIgnoreCase("in")) {
							result[0] = method2.invoke(classInstance, insertedBody);
						} else {
							result[0] = method2.invoke(classInstance, insertedBody, insertedInput);
						}
						logger.info("Function with ID: {} has been executed successfully", function.getId());

					} catch (ThreadDeath e) {
						e.printStackTrace();

					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {// in case there exception invoked by the user
						// TODO Auto-generated catch block
						if (e.getCause() != null && e.getCause().getClass().getCanonicalName()
								.equalsIgnoreCase("grizzlyApi.ErrorException")) {
							exception[0] = e.getCause().getMessage();
							try {
								Field declaredField = e.getCause().getClass().getDeclaredField("httpCode");
								httpCode[0] = (int) declaredField.get(e.getCause());
								logger.info(
										"Client Exception inside the function with ID: {} has been thrown successfully with httpCode: {}",
										function.getId(), httpCode[0]);

							} catch (NoSuchFieldException | SecurityException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IllegalArgumentException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IllegalAccessException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

						} else {
							e.printStackTrace();

						}
					}
				}
			});
			functionThread.start();
			// synchronize our threads
			functionThread.join();

		} catch (UnrecognizedPropertyException e) {
			// if there is mismatch between input and our function's parameters
			return new Response.ResponseBuilder().type("exception").response(new Gson().toJson(e.getMessage())).build();
		} catch (IllegalArgumentException e) {
			return new Response.ResponseBuilder().type("exception").response(new Gson().toJson(e.getMessage())).build();
		} catch (SecurityException e) {// in case of security issues
			return new Response.ResponseBuilder().type("exception")
					.response("operation not permitted, change your function !").build();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			return new Response.ResponseBuilder().type("exception").response(
					"Please check if inserted information matches your class declaration, and verify if the class and method are well defined ")
					.build();
		} catch (NoSuchMethodException e1) {
			e1.printStackTrace();
			return new Response.ResponseBuilder().type("exception")
					.response("Verify if you are using the convetional syntax" + e1.getMessage()).build();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
		}
		if (result[1] != null) {// result[1] contains server exceptions and errors
			// response type is an intern type allow us to delegate exception handler to the
			// controller
			return new Response.ResponseBuilder().type("security").response(new Gson().toJson(result[1])).build();

		} else { // if output's type is among those type we can consider it valid response
			if (result[0] != null && (result[0] instanceof Number || result[0] instanceof String
					|| result[0] instanceof Boolean || result[0] instanceof Character)) {
				return new Response.ResponseBuilder().type("valid").response(result[0]).build();

			} else if (httpCode[0] != -1) {// if there is exception thrown by the client
				return new Response.ResponseBuilder().type("Exception").httpCode(httpCode[0]).response(exception[0])
						.build();
			} else if (result[0] != null) {// if our result is a custom object or

				// Map or an array so we can serialize it to json and send it
				return new Response.ResponseBuilder().type("valid").response(new Gson().toJson(result[0])).build();
			} else { // if response is null, we can consider it as function's logical or syntaxical
						// problem and we return 400
				return new Response.ResponseBuilder().type("exception").httpCode(400)
						.response("there is problem with the function :  " + function.getName()).build();
			}
		}

	}

}
