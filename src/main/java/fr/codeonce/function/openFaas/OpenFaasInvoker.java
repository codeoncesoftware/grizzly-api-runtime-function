package fr.codeonce.function.openFaas;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import fr.codeonce.function.dto.Response;

@ApplicationScoped
public class OpenFaasInvoker {

	private Client client = ClientBuilder.newClient();;

	public OpenFaasInvoker() {

	};

	public Object invokeOpenFaas(OpenFaasRequest req) {

		System.out.println(req.getUri());
		System.out.println(req.getHeaders());
		System.out.println(req.getBody());

		MultivaluedMap<String, Object> myHeaders = null;
		if (req.getHeaders() != null) {
			req.getHeaders().forEach((k, v) -> {
				myHeaders.add(k, v);
			});
		}
		String result = null;
		try {
			Builder invoker = client.target(req.getUri()).request().headers(myHeaders);
			result = invoker.post(Entity.entity(req.getBody(), MediaType.APPLICATION_JSON)).readEntity(String.class);
		} catch (Exception e) {
			return new Response.ResponseBuilder()// 136

					.httpCode(400)//
					.response(e.getMessage()).type("exception").build();
		}

		return new Response.ResponseBuilder()// 136

				.httpCode(200)//
				.response(result).type("valid").build();

	}
}
