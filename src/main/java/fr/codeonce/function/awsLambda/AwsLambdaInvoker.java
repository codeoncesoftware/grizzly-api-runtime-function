package fr.codeonce.function.awsLambda;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.directory.model.ServiceException;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.AWSLambdaException;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.codeonce.function.dto.Response;
import fr.codeonce.function.service.JavaScriptCompilerService;

@ApplicationScoped
public class AwsLambdaInvoker {

	final Logger logger = LoggerFactory.getLogger(JavaScriptCompilerService.class);

	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private static final int STATUS_CODE_ERROR = 300;

	public Object invokeAWSLambda(AWSLambdaRequest req) throws Exception {
		String functionName = req.getFunctionName();

		InvokeRequest invokeRequest = new InvokeRequest().withFunctionName(functionName).withPayload(req.getPayload());
		System.out.println("second step !");

		InvokeResult invokeResult = null;
		AWSCredentials awsCredentials = req.getAwsCredentials();

		invokeResult = invoke(awsCredentials, invokeRequest);
		if (invokeResult.getFunctionError() != null) {
			return new Response.ResponseBuilder()//
					.httpCode(invokeResult.getStatusCode())//
					.response(invokeResult.getFunctionError()).type("aws-lambda").build();
		}
		System.out.println("third step !");

		String ans = new String(invokeResult.getPayload().array(), StandardCharsets.UTF_8);
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", (invokeResult.getSdkHttpMetadata().getHttpHeaders().get("Content-Type")));
		return new Response.ResponseBuilder()// 136

				.httpCode(invokeResult.getStatusCode())//
				.response(ans).type("valid").build();
	};

	// @CacheResult(cacheName = "aws-cache")
	public Optional<AWSLambda> createSession(AWSCredentials awsCredentials) throws Exception {
		com.amazonaws.auth.BasicAWSCredentials sessionCredentials = new com.amazonaws.auth.BasicAWSCredentials(awsCredentials.getAwsAccessKeyId(),
				awsCredentials.getAwsSecretAccess());
		Regions regionAws = null;
		try {
			regionAws = Regions.valueOf(awsCredentials.getRegion());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new Exception("AWS Region Does Not Exist !");
		}

		if (!Region.getRegion(Regions.US_WEST_2).isServiceSupported(AWSLambda.ENDPOINT_PREFIX)) {
			return Optional.empty();
		}

		AWSLambda awsLambda = AWSLambdaClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(sessionCredentials)).withRegion(regionAws)
				.build();
		return Optional.ofNullable(awsLambda);

	}

	public InvokeResult invoke(AWSCredentials awsCredeniitals, InvokeRequest invokeRequest) throws Exception {
		try {
			Optional<AWSLambda> awsLambda = createSession(awsCredeniitals);
			System.out.println("my step !");
			
			return awsLambda.map((a) -> a.invoke(invokeRequest))
					.orElseThrow(() -> new Exception("Credentials are not valid !"));

		} catch (ServiceException e) {
			logger.error(e.getMessage());
			throw new AWSLambdaException(e.getMessage());

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new AWSLambdaException(e.getMessage());

		}
	}

}
