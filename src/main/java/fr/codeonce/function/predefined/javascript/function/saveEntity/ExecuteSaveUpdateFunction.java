package fr.codeonce.function.predefined.javascript.function.saveEntity;

import javax.enterprise.context.ApplicationScoped;

import org.graalvm.polyglot.Context;

@ApplicationScoped
public class ExecuteSaveUpdateFunction {

	public void evalMongoSaveUpdateFunction(String mongoString, String collectionName, String dbName, String provider,
			Context context) {

		ExecuteSaveUpdateQueryService service = new ExecuteSaveUpdateQueryService(mongoString, collectionName, provider,
				dbName);
		context.getBindings("js").putMember("_saveService", service);
		context.eval("js",
				"_insertOne=(object,mongoConfig)=>{ let document;  if(mongoConfig!=null) {   document=_saveService.executeSaveQuery(JSON.stringify(object),mongoConfig.dbName,mongoConfig.collectionName);} else { document=_saveService.executeSaveQuery(JSON.stringify(object),null,null);  } return document;}");
		context.eval("js",
				"_insertMany=(objects,mongoConfig)=>{ let documents;   if(mongoConfig!=null){ documents =_saveService.executeSaveAllQuery(JSON.stringify(objects),mongoConfig.dbName,mongoConfig.collectionName); } else {documents =_saveService.executeSaveAllQuery(JSON.stringify(objects,null,null));} return documents;}");

		context.eval("js",
				"_updateOne=(id,object,mongoConfig)=>{ let document;  if(mongoConfig!=null) {   document=_saveService.executeUpdateQuery(id,JSON.stringify(object),mongoConfig.dbName,mongoConfig.collectionName);} else { document=_saveService.executeUpdateQuery(JSON.stringify(object),null,null);  } return document;}");
		context.eval("js",
				"_updateMany=(filtre,update,mongoConfig)=>{ let documents;   if(mongoConfig!=null){ documents =_saveService.executeSaveAllQuery(filtre,update,mongoConfig.dbName,mongoConfig.collectionName); } else {documents =_saveService.executeSaveAllQuery(JSON.stringify(objects,null,null));} return documents;}");
	}

}
