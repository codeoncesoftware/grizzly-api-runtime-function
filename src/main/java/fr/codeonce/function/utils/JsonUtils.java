package fr.codeonce.function.utils;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

	public static Boolean isValidJson(String maybeJson) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			mapper.readTree(maybeJson);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

}
