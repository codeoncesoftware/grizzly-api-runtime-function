package fr.codeonce.function.predefined.javascript.function.saveEntity;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.config.ConfigProvider;
import org.graalvm.polyglot.HostAccess;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.internal.MongoClientImpl;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;

public class ExecuteSaveUpdateQueryService {

	private String uri;
	private String collectionName;
	private String provider;
	private String dbName;

	String atlasUri;

	@HostAccess.Export
	public Object executeSaveQuery(String query, String insertedDbName, String insertedCollectionName)
			throws Exception {
		if (uri != null) {
			return executeSaveQueryMongo(query, insertedDbName, insertedCollectionName, false);
		} else {
			return executeSaveQueryMongo(query, insertedDbName, insertedCollectionName, true);
		}
	}

	@HostAccess.Export
	public Object executeUpdateQuery(String id, String query, String insertedDbName, String insertedCollectionName)
			throws Exception {
		if (uri != null) {
			return executeUpdateQueryMongo(id, query, insertedDbName, insertedCollectionName, false);
		} else {
			return executeUpdateQueryMongo(id, query, insertedDbName, insertedCollectionName, true);
		}
	}

	@HostAccess.Export
	public Object executeSaveAllQuery(String query, String insertedDbName, String insertedCollectionName)
			throws Exception {
		if (uri != null) {
			return executeSaveAllQueryMongo(query, insertedDbName, insertedCollectionName, false);
		} else {
			return executeSaveAllQueryMongo(query, insertedDbName, insertedCollectionName, true);
		}
	}

	private Object executeSaveQueryMongo(String object, String insertedDbName, String insertedCollectionName,
			boolean isFree) throws Exception {

		try {
			JSONObject jsonObject = new JSONObject(object.toString());
		} catch (Exception e) {
			throw new Exception(" Invalid parameters of _insertOne() !");
		}
		dbName = (insertedDbName != null && !insertedDbName.isBlank()) ? insertedDbName : dbName;
		collectionName = (insertedCollectionName != null && !insertedCollectionName.isBlank()) ? insertedCollectionName
				: collectionName;

		MongoClient mongoClient = prepareMongoClient(isFree);
		MongoCollection<Document> mongoCollection = mongoClient.getDatabase(dbName).getCollection(collectionName);
		InsertOneResult savedObject = mongoCollection.insertOne(Document.parse(object));
		return savedObject.getInsertedId().asObjectId().getValue().toString();
	}

	private Object executeSaveAllQueryMongo(String object, String insertedDbName, String insertedCollectionName,
			boolean isFree) throws Exception {

		MongoClient mongoClient = prepareMongoClient(isFree);
		List<String> result = new ArrayList<>();
		InsertManyResult savedObject = null;
		dbName = (insertedDbName != null && !insertedDbName.isBlank()) ? insertedDbName : dbName;
		collectionName = (insertedCollectionName != null && !insertedCollectionName.isBlank()) ? insertedCollectionName
				: collectionName;
		MongoCollection<Document> mongoCollection = mongoClient.getDatabase(dbName).getCollection(collectionName);
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject("{\"list\": " + object.toString() + "}");
			JSONArray jsonArray = jsonObject.getJSONArray("list");
			List<Document> documents = new ArrayList<Document>();
			for (int i = 0; i < jsonArray.length(); i++) {
				documents.add(Document.parse(jsonArray.getJSONObject(i).toString()));

			}
			savedObject = mongoCollection.insertMany(documents);

			for (int i = 0; i < savedObject.getInsertedIds().size(); i++) {
				result.add(savedObject.getInsertedIds().get(i).asObjectId().getValue().toString());

			}
		} catch (JSONException e) {
			throw new Exception(" Invalid parameters of _saveAllObjects() !");
		}
		return result;
	}

	private Object executeUpdateQueryMongo(String id, String object, String insertedDbName,
			String insertedCollectionName, boolean isFree) throws Exception {

		ObjectId objID = new ObjectId(id);
		Document idFiltre = new Document().append("_id", objID);
		dbName = (insertedDbName != null && !insertedDbName.isBlank()) ? insertedDbName : dbName;
		collectionName = (insertedCollectionName != null && !insertedCollectionName.isBlank()) ? insertedCollectionName
				: collectionName;
		MongoClient mongoClient = prepareMongoClient(isFree);
		MongoCollection<Document> mongoCollection = mongoClient.getDatabase(dbName).getCollection(collectionName);
		UpdateResult updatedObject = mongoCollection.replaceOne(idFiltre, Document.parse(object));

		return updatedObject.getModifiedCount();
	}

	private Object executeUpdateAllQueryMongo(String filtre, String update, String insertedDbName,
			String insertedCollectionName, boolean isFree) throws Exception {

		MongoClient mongoClient = prepareMongoClient(isFree);
		UpdateResult updatedObjects = null;
		dbName = (insertedDbName != null && !insertedDbName.isBlank()) ? insertedDbName : dbName;
		collectionName = (insertedCollectionName != null && !insertedCollectionName.isBlank()) ? insertedCollectionName
				: collectionName;
		MongoCollection<Document> mongoCollection = mongoClient.getDatabase(dbName).getCollection(collectionName);

		updatedObjects = mongoCollection.updateMany(Document.parse(filtre), Document.parse(update));
		return updatedObjects.getUpsertedId().asObjectId().getValue().toString();
	}

	private MongoClient prepareMongoClient(boolean isFree) {
		MongoClient mongoClient = null;

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
		return mongoClient;
	}

	public ExecuteSaveUpdateQueryService(String uri, String collectionName, String provider, String dbName) {
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
