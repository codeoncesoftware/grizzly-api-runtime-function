package fr.codeonce.function.awsLambda;

import fr.codeonce.function.dto.FunctionInvokeRequest;

public class AWSLambdaRequest implements FunctionInvokeRequest {

	private String type;
	private String functionName;
	private AWSCredentials awsCredentials;
	private String payload;

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the functionName
	 */
	public String getFunctionName() {
		return functionName;
	}

	/**
	 * @param functionName the functionName to set
	 */
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	/**
	 * @return the awsCredentials
	 */
	public AWSCredentials getAwsCredentials() {
		return awsCredentials;
	}

	/**
	 * @param awsCredentials the awsCredentials to set
	 */
	public void setAwsCredentials(AWSCredentials awsCredentials) {
		this.awsCredentials = awsCredentials;
	}

	/**
	 * @return the payload
	 */
	public String getPayload() {
		return payload;
	}

	/**
	 * @param payload the payload to set
	 */
	public void setPayload(String payload) {
		this.payload = payload;
	}

}
