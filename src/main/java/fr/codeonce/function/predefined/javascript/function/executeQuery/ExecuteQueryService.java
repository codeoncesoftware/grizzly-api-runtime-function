package fr.codeonce.function.predefined.javascript.function.executeQuery;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.eclipse.microprofile.config.ConfigProvider;
import org.graalvm.polyglot.HostAccess;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.internal.MongoClientImpl;

public class ExecuteQueryService {

	private String uri;
	private String collectionName;
	private String provider;
	private String dbName;

	String atlasUri;

	@HostAccess.Export
	public List<String> executeCustomQuery(Object query, String insertedDbName, String insertedColletionName)
			throws Exception {
		if (!(query instanceof String)) {
			throw new Exception("invalid parameters for executeQuery()");
		}
		if (uri != null) {
			return mongoConnection(query, insertedDbName, insertedColletionName, false);
		} else {
			return mongoConnection(query, insertedDbName, insertedColletionName, true);
		}
	}

	@HostAccess.Export
	public List<String> executeCustomQuery(Object query) throws Exception {
		if (!(query instanceof String)) {
			throw new Exception("invalid parameters for executeQuery()");
		}
		if (uri != null) {
			return mongoConnection(query, null, null, false);
		} else {
			return mongoConnection(query, null, null, true);
		}
	}

	private List<String> mongoConnection(Object query, String insertedDbName, String insertedColletionName,
			boolean isFree) {
		;
		MongoClient mongoClient = null;
		if (provider.equalsIgnoreCase("MONGO")) {

		}
		if (isFree) {
			ConnectionString connectionString = new ConnectionString(atlasUri);
			MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connectionString)
					.build();
			mongoClient = new MongoClientImpl(settings, null);
		} else {
			MongoClientSettings settings;
			try {
				ConnectionString connectionString = new ConnectionString(
						uri + "/" + "?authSource=admin&authMechanism=SCRAM-SHA-1");
				settings = MongoClientSettings.builder().applyConnectionString(connectionString).build();
			} catch (Exception e) {
				ConnectionString connectionString = new ConnectionString(uri);
				settings = MongoClientSettings.builder().applyConnectionString(connectionString).build();

			}

			mongoClient = new MongoClientImpl(settings, null);
		}
		dbName = (insertedDbName != null && !insertedDbName.isBlank()) ? insertedDbName : dbName;
		collectionName = (insertedColletionName != null && !insertedColletionName.isBlank()) ? insertedColletionName
				: collectionName;
		MongoCollection<Document> mongoCollection = mongoClient.getDatabase(dbName).getCollection(collectionName);
		FindIterable<Document> find = mongoCollection.find(Document.parse((String) query));
		List<String> documents = new ArrayList();
		find.forEach(d -> documents.add(d.toJson()));
		return documents;
	}

	public ExecuteQueryService(String uri, String collectionName, String provider, String dbName) {
		super();
		this.uri = uri;
		this.collectionName = collectionName;
		this.provider = provider;
		this.dbName = dbName;
		// this.atlasUri = ConfigProvider.getConfig().getValue("atlasMongo", String.class);
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

}