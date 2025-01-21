package fr.codeonce.function.predefined.javascript.function.executeQuery;

import javax.enterprise.context.ApplicationScoped;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

@ApplicationScoped
public class ExecuteQueryFunction {

	public void evalMongoExecuteQueryFunction(String mongoString, String collectionName, String dbName, String provider,
			Context context) {

		ExecuteQueryService service = new ExecuteQueryService(mongoString, collectionName, provider, dbName);
		context.getBindings("js").putMember("services", service);
		context.eval("js",
				"_executeQuery=(query,mongoConfig)=>{ let documents; if(mongoConfig!=null){ documents =services.executeCustomQuery(query,mongoConfig.dbName,mongoConfig.collectionName);} else{documents =services.executeCustomQuery(query,null,null);}   return JSON.parse(documents);}");

	}

	public void evalElasticExecuteQueryFunction(String mongoString, String collectionName, String dbName,
			String provider, Context context) {
		ExecuteQueryService services = new ExecuteQueryService(mongoString, collectionName, provider, dbName);
		context.getBindings("js").putMember("services", services);
		Value findFunction = context.eval("js",
				"_executeQuery=(query)=>{ let documents =services.executeCustomQuery(query);  return JSON.parse(documents);}");
	}

}
