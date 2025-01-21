package fr.codeonce.function.predefined.javascript.function.httpRequest;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.graalvm.polyglot.HostAccess;
import org.jboss.logging.Logger;
import org.json.JSONArray;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpClientService {
	
	private static final Logger log = Logger.getLogger(HttpClientService.class);


	private Client client;

	public HttpClientService(Client client) {
		this.client = client;
	}
   // URI est un champs obligatoire
	@HostAccess.Export
	public Object httpRequestGet(Object options) throws Exception {
		Map<String, Object> optionsMap;
		MultivaluedMap<String, Object> myHeaders;

		// verify the type of arguments
		try {
			optionsMap = (Map) options;

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			throw new IllegalArgumentException("Illegal Argument of _httpGet()");
		}

		// send get request
		try {
			if (optionsMap.get("headers") == null) {
				myHeaders = new MultivaluedHashMap();

			} else {
				myHeaders = new MultivaluedHashMap((Map) optionsMap.get("headers"));

			}

			Builder invocationBuilder = client.target((String) optionsMap.get("uri")).request().headers(myHeaders);
			return invocationBuilder.get(String.class);

		} catch (ProcessingException e) {
			if (e.getCause() instanceof UnknownHostException) {
				throw new Exception("_httpGet(): No such host is known");
			}
			throw new Exception("_httpGet(): There is a porblem");

		} catch (Exception e) {
			throw e;
		}

	}

	@HostAccess.Export
	public Object httpRequestPost(Object options, String object) throws Exception {
		Map<String, Object> objectMap = null;
		List<String> objectArray = null;
		Map<String, Object> optionsMap;
		MultivaluedMap<String, Object> myHeaders;
		// verify the type of arguments
		try {

			optionsMap = (Map) options;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new IllegalArgumentException("Illegal Argument of _httpPost()");
		}

		try {
			objectMap = new ObjectMapper().readValue(object, Map.class);
		} catch (Exception e1) {
			try {
				JSONArray jsonArray = new JSONArray(object);
				if (jsonArray != null) {
					objectArray = new ArrayList<String>();
					for (int i = 0; i < jsonArray.length(); i++) {
						objectArray.add(jsonArray.getString(i));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new IllegalArgumentException("Invalid body format of _httpPost()");
			}

		}

		// send post request
		try {
			if (optionsMap.get("headers") == null) {
				myHeaders = new MultivaluedHashMap();

			} else {
				myHeaders = new MultivaluedHashMap((Map) optionsMap.get("headers"));
			}
			Builder invocationBuilder = client.target((String) optionsMap.get("uri")).request().headers(myHeaders);
			return (objectArray == null)
					? invocationBuilder.post(Entity.entity(object, MediaType.APPLICATION_JSON))
							.readEntity(String.class)
					: invocationBuilder.post(Entity.entity(objectArray.toString(), MediaType.APPLICATION_JSON))
							.readEntity(String.class);

		} catch (ProcessingException e) {
			if (e.getCause() instanceof UnknownHostException) {
				throw new Exception("_httpPost(): No such host is known");
			}
			throw new Exception("_httpPost(): There is a porblem: "+e);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

	@HostAccess.Export
	public Object httpRequestPut(Object options, String object) throws Exception {
		Map<String, Object> objectMap = null;
		List<String> objectArray = null;
		Map<String, Object> optionsMap;
		MultivaluedMap<String, Object> myHeaders;
		// verify the type of arguments
		try {

			optionsMap = (Map) options;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new IllegalArgumentException("Illegal Argument of _httpPut()");
		}

		try {
			objectMap = new ObjectMapper().readValue(object, Map.class);

		} catch (Exception e1) {
			try {
				JSONArray jsonArray = new JSONArray(object);
				if (jsonArray != null) {
					objectArray = new ArrayList<String>();
					for (int i = 0; i < jsonArray.length(); i++) {
						objectArray.add(jsonArray.getString(i));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new IllegalArgumentException("Invalid body format of _httpPut()");
			}

		}

		// send post request
		try {
			if (optionsMap.get("headers") == null) {
				myHeaders = new MultivaluedHashMap();

			} else {
				myHeaders = new MultivaluedHashMap((Map) optionsMap.get("headers"));
			}
			
			Builder invocationBuilder = client.target((String) optionsMap.get("uri")).request().headers(myHeaders);
			return (objectArray == null)
					? invocationBuilder.put(Entity.entity(object, MediaType.APPLICATION_JSON))
							.readEntity(String.class)
					: invocationBuilder.put(Entity.entity(objectArray.toString(), MediaType.APPLICATION_JSON))
							.readEntity(String.class);

		} catch (ProcessingException e) {
			if (e.getCause() instanceof UnknownHostException) {
				throw new Exception("_httpPut(): No such host is known: "+e);
			}
			throw new Exception("_httpPut(): There is a porblem: "+e);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

	@HostAccess.Export
	public Object httpRequestDelete(Object options, String object) throws Exception {
		Map<String, Object> objectMap = null;
		List<String> objectArray = null;
		Map<String, Object> optionsMap;
		MultivaluedMap<String, Object> myHeaders;
		// verify the type of arguments
		try {

			optionsMap = (Map) options;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new IllegalArgumentException("Illegal Argument of _httpDelete()");
		}

		try {
			if (object != null) {
				objectMap = new ObjectMapper().readValue(object, Map.class);
			}
		} catch (Exception e1) {

			throw new IllegalArgumentException("Invalid body format of _httpDelete()");

		}

		// send delete request
		try {
			if (optionsMap.get("headers") == null) {
				myHeaders = new MultivaluedHashMap();

			} else {
				myHeaders = new MultivaluedHashMap((Map) optionsMap.get("headers"));
			}
			Builder invocationBuilder = client.target((String) optionsMap.get("uri")).request().headers(myHeaders);
			return invocationBuilder.delete();

		} catch (ProcessingException e) {
			if (e.getCause() instanceof UnknownHostException) {
				throw new Exception("_httpDelete(): No such host is known");
			}
			throw new Exception("_httpDelete(): There is a porblem: "+e);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

}
