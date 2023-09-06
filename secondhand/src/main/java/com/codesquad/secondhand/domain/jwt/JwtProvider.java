package com.codesquad.secondhand.domain.jwt;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {

	@Value("${jwt.token.secret-key}")
	private String signature;
	@Value("${jwt.token.sign-up.secret-key}")
	private String signUpSignature;
	private byte[] secret;
	private Key key;
	private byte[] signUpSecret;
	private Key signUpKey;

	@PostConstruct
	public void setSecretKey() {
		secret = signature.getBytes();
		key = Keys.hmacShaKeyFor(secret);

		signUpSecret = signUpSignature.getBytes();
		signUpKey = Keys.hmacShaKeyFor(signUpSecret);
	}

	private String createToken(Map<String, Object> claims, Date expireDate) {
		long currentTimeMillis = System.currentTimeMillis();

		Map<String, Object> modifiableClaims = new HashMap<>(claims);
		modifiableClaims.put("createdMillis", currentTimeMillis);

		Map<String, Object> immutableClaims = Collections.unmodifiableMap(modifiableClaims);

		return Jwts.builder()
			.setClaims(immutableClaims)
			.setExpiration(expireDate)
			.signWith(key)
			.compact();
	}

	private String createSignUpToken(Map<String, Object> claims, Date expireDate) {
		return Jwts.builder()
			.setClaims(claims)
			.setExpiration(expireDate)
			.signWith(signUpKey)
			.compact();
	}

	public Claims getClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	public Claims getClaimsFromSignUpToken(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(signUpKey)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	public Jwt createTokens(Map<String, Object> claims) {
		String accessToken = createToken(claims, getExpireDateAccessToken());
		String refreshToken = createToken(new HashMap<>(), getExpireDateRefreshToken());
		return new Jwt(accessToken, refreshToken);
	}

	public Jwt createSignUpToken(Map<String, Object> claims) {
		String signUpToken = createSignUpToken(claims, getExpireDateAccessToken());
		return new Jwt(signUpToken);
	}

	public Jwt reissueAccessToken(Map<String, Object> claims, String refreshToken) {
		String accessToken = createToken(claims, getExpireDateAccessToken());
		return new Jwt(accessToken, refreshToken);
	}

	private Date getExpireDateAccessToken() {
		long expireTimeMils = 1000L * 60 * 60;
		return new Date(System.currentTimeMillis() + expireTimeMils);
	}

	private Date getExpireDateRefreshToken() {
		long expireTimeMils = 1000L * 60 * 60 * 24 * 60;
		return new Date(System.currentTimeMillis() + expireTimeMils);
	}

}
