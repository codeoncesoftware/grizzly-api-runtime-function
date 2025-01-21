package fr.codeonce.function.predefined.javascript.function.jwt;

import java.security.Key;
import java.util.Base64;

import org.graalvm.polyglot.HostAccess;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public class ParseJwtTokenService {

	public ParseJwtTokenService() {
		super();

	}

	@HostAccess.Export
	public Object decryptJwtToken(Object privateKey, Object token) {
		if (!(privateKey instanceof String && token instanceof String)) {
			throw new IllegalArgumentException("Invalid arguments for _verifyJwt()!");
		}
		String[] chunks = ((String) token).split("\\.");
		Base64.Decoder decoder = Base64.getDecoder();
		String header = new String(decoder.decode(chunks[0]));
		String payload = new String(decoder.decode(chunks[1]));
		return payload;

	}

	@HostAccess.Export
	public Object verifytJwtToken(Object privateKey, Object token) {

		if (!(privateKey instanceof String && token instanceof String)) {
			throw new IllegalArgumentException("Invalid arguments for _verifyJwt()!");
		}

		try {
			Key key = toSecretKey((String) privateKey);
			System.out.println(privateKey);
			Jwts.parser().setSigningKey(key).parseClaimsJws((String) token).getBody();
			return true;
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			return false;
		} catch (ExpiredJwtException e) {

			return false;
		} catch (UnsupportedJwtException e) {

			return false;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	private static Key toSecretKey(String secretKey) {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

}
