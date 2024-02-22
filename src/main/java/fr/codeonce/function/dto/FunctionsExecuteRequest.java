package fr.codeonce.function.dto;

public class FunctionsExecuteRequest {

	private RuntimeResourceFunction function;
	private String jsonRequestModel;
	private String pojoRequestModel;
	private String executionType;
	private String jsonPersistanceModel;
	private float lastUpdatRequestModel;
	private String mongoURI;
	private String collectionName;
	private String dbName;
	private String provider;
	private String logId;

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getMongoURI() {
		return mongoURI;
	}

	public void setMongoURI(String mongoURI) {
		this.mongoURI = mongoURI;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public float getLastUpdatRequestModel() {

		return lastUpdatRequestModel;
	}

	public void setLastUpdatRequestModel(float lastUpdatRequestModel) {
		this.lastUpdatRequestModel = lastUpdatRequestModel;
	}

	public String getJsonPersistanceModel() {

		return jsonPersistanceModel;
	}

	public void setJsonPersistanceModel(String jsonPersistanceModel) {
		this.jsonPersistanceModel = jsonPersistanceModel;
	}

	public String getJsonRequestModel() {
		return jsonRequestModel;
	}

	public void setJsonRequestModel(String jsonRequestModel) {
		this.jsonRequestModel = jsonRequestModel;
	}

	public String getPojoRequestModel() {
		return pojoRequestModel;
	}

	public void setPojoRequestModel(String pojoRequestModel) {
		this.pojoRequestModel = pojoRequestModel;
	}

	public String getExecutionType() {
		return executionType;
	}

	public void setExecutionType(String executionType) {
		this.executionType = executionType;
	}

	public RuntimeResourceFunction getFunction() {
		return function;
	}

	public void setFunction(RuntimeResourceFunction function) {
		this.function = function;
	}

}
