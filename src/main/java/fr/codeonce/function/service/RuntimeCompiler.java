package fr.codeonce.function.service;

import org.graalvm.polyglot.Engine;

import fr.codeonce.function.dto.Response;
import fr.codeonce.function.dto.RuntimeResourceFunction;

public interface RuntimeCompiler {

	/**
	 * function responsable for the execution of the scripts
	 * 
	 * @param function
	 * @param body
	 * @param args
	 * @param engine
	 * @return
	 * @throws Exception 
	 */
	public Response execute(RuntimeResourceFunction function, String requestObjectString,
			String persistanceObjectString, String pojoObjectString, float LastUpdateRequestModel, String executiontype,
			String mongoURI, String collectionName, String dbName, String provider, String logId, Engine engine) throws Exception;

}
