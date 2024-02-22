package fr.codeonce.function.predefined.javascript.function.logsFunction;

import javax.enterprise.context.ApplicationScoped;

import org.graalvm.polyglot.Context;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationScoped
public class LoggerFunction {
	@Autowired
	com.mongodb.client.MongoClient MongoClient;

	public void evalLoggerFunction(String id, String projectId, String functionName, Context context) {

		String errorFunction = String.format(" _error=(error)	=>{ loggerService.error('%s','%s','%s',error)} ", id,
				projectId, functionName);
		String infoFunction = String.format(" _info=(info)=>{ loggerService.info('%s','%s','%s',info)}", id, projectId,
				functionName);

		LoggerService loggerService = new LoggerService(id, functionName, MongoClient);
		context.getBindings("js").putMember("loggerService", loggerService);
		context.eval("js", infoFunction);
		context.eval("js", errorFunction);

	}

}
