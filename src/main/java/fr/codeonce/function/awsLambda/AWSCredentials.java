package fr.codeonce.function.awsLambda;

public class AWSCredentials {

	private String type;
	private String awsAccessKeyId;
	private String awsSecretAccess;
	private String awsSessionToken;
	private String region;

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
	 * @return the awsAccessKeyId
	 */
	public String getAwsAccessKeyId() {
		return awsAccessKeyId;
	}

	/**
	 * @param awsAccessKeyId the awsAccessKeyId to set
	 */
	public void setAwsAccessKeyId(String awsAccessKeyId) {
		this.awsAccessKeyId = awsAccessKeyId;
	}

	/**
	 * @return the awsSecretAccess
	 */
	public String getAwsSecretAccess() {
		return awsSecretAccess;
	}

	/**
	 * @param awsSecretAccess the awsSecretAccess to set
	 */
	public void setAwsSecretAccess(String awsSecretAccess) {
		this.awsSecretAccess = awsSecretAccess;
	}

	/**
	 * @return the awsSessionToken
	 */
	public String getAwsSessionToken() {
		return awsSessionToken;
	}

	/**
	 * @param awsSessionToken the awsSessionToken to set
	 */
	public void setAwsSessionToken(String awsSessionToken) {
		this.awsSessionToken = awsSessionToken;
	}

	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * @param region the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((awsAccessKeyId == null) ? 0 : awsAccessKeyId.hashCode());
		result = prime * result + ((awsSecretAccess == null) ? 0 : awsSecretAccess.hashCode());
		result = prime * result + ((awsSessionToken == null) ? 0 : awsSessionToken.hashCode());
		result = prime * result + ((region == null) ? 0 : region.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AWSCredentials other = (AWSCredentials) obj;
		if (awsAccessKeyId == null) {
			if (other.awsAccessKeyId != null)
				return false;
		} else if (!awsAccessKeyId.equals(other.awsAccessKeyId))
			return false;
		if (awsSecretAccess == null) {
			if (other.awsSecretAccess != null)
				return false;
		} else if (!awsSecretAccess.equals(other.awsSecretAccess))
			return false;
		if (awsSessionToken == null) {
			if (other.awsSessionToken != null)
				return false;
		} else if (!awsSessionToken.equals(other.awsSessionToken))
			return false;
		if (region == null) {
			if (other.region != null)
				return false;
		} else if (!region.equals(other.region))
			return false;
		return true;
	}

}
