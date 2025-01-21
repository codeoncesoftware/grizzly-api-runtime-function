package fr.codeonce.function.predefined.javascript.function.logsFunction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bson.Document;
import org.graalvm.polyglot.HostAccess;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;

public class LoggerService {

	private String id;
	private String functionName;
	private String projectId;
	private MongoClient mongoClient;

	public LoggerService(String id, String functionName, MongoClient mongoClient) {
		super();
		this.id = id;
		this.functionName = functionName;
		this.mongoClient = mongoClient;
	}

	@HostAccess.Export
	public void info(String source, String projectId, String functionName, Object message) {
		if (!(message instanceof String)) {
			throw new IllegalArgumentException("Illegal arguments for _info()");
		}
		Date date = new Date();
		SimpleDateFormat dateFor = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
		try {
			addLog(source, projectId, (String) message, "INFO", dateFor.parse(dateFor.format(date)));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@HostAccess.Export
	public void error(String source, String projectId, String functionName, Object message) {
		if (!(message instanceof String)) {
			throw new IllegalArgumentException("Illegal arguments for _error()");
		}
		Date date = new Date();
		SimpleDateFormat dateFor = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
		try {
			addLog(source, projectId, (String) message, "ERROR", dateFor.parse(dateFor.format(date)));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Object addLog(String source, String projectId, String message, String logLevel, Date time) {

		Document document = new Document();
		document.put("projectId", projectId);
		document.put("message", message);
		document.put("logLevel", logLevel);
		document.put("time", time);
		document.put("source", source);
		return getCollection().insertOne(document);
	}

	private MongoCollection<Document> getCollection() {
		return mongoClient.getDatabase("grizzly-api").getCollection("log");
	}

}
