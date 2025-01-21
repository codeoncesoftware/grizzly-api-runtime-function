package fr.codeonce.function.openFaas;

import java.util.List;
import java.util.Map;

public class OpenFaasInfo {
	private String host;
	private String port;
	private String uri;
	private List<Map<String, Object>> headers;

	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * @return the headers
	 */
	public List<Map<String, Object>> getHeaders() {
		return headers;
	}

	/**
	 * @param headers the headers to set
	 */
	public void setHeaders(List<Map<String, Object>> headers) {
		this.headers = headers;
	}
}
