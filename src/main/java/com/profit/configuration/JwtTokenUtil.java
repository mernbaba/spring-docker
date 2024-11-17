package com.profit.configuration;

import java.io.Serializable;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.profit.datamodel.SecUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = -2550185165626007488L;

	public static final long JWT_TOKEN_VALIDITY = 7 * 24 * 60 * 60;

	@Value("${jwt.secret}")
	private String secret;

	// retrieve username from jwt token
	public String getUsernameFromToken(String token) {
		String user = "";
		try {
			user = getClaimFromToken(token, Claims::getSubject);
		} catch (Exception e) {
		}

		return user;
	}

	// retrieve expiration date from jwt token
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	// for retrieveing any information from token we will need the secret key
	public Claims getAllClaimsFromToken(String token) {
		Claims claims = null;
		try {
			String decoder = Base64.getEncoder().encodeToString(secret.getBytes());
			claims = Jwts.parser().setSigningKey(decoder).parseClaimsJws(token).getBody();
			return claims;
		} catch (io.jsonwebtoken.MalformedJwtException e) {
		}
		return claims;
	}

	// check if the token has expired
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	// generate token for user
	public String generateToken(UserDetails userDetails, SecUser secuser) {

		return doGenerateToken(userDetails, secuser);
	}

	// while creating the token -
	// 1. Define claims of the token, like Issuer, Expiration, Subject, and the ID
	// 2. Sign the JWT using the HS512 algorithm and secret key.
	// 3. According to JWS Compact
	// Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
	// compaction of the JWT to a URL-safe string
	private String doGenerateToken(UserDetails userDetails, SecUser secuser) {

		String subject = userDetails.getUsername();
		Map<String, Object> payloadMap = new HashMap<>();
//		payloadMap.put("name", secuser.getUserName());
		payloadMap.put("company", secuser.getCompanyCode());
		payloadMap.put("branch", secuser.getBranchCode());
		payloadMap.put("userType", secuser.getUserType());

		String encodedString = Base64.getEncoder().encodeToString(secret.getBytes());
		return Jwts.builder().setClaims(payloadMap).setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
				.signWith(SignatureAlgorithm.HS512, encodedString).compact();
	}

	// validate token
	public Boolean validateToken(String token, UserDetails userDetails) throws Exception {
		final String username = getUsernameFromToken(token);
		if (isTokenExpired(token)) {
			throw new Exception("Access_token_expired");
		}
		return (username.equals(userDetails.getUsername()));
	}
}