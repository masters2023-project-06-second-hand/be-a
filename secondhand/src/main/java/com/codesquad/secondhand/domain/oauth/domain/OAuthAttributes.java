package com.codesquad.secondhand.domain.oauth.domain;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public enum OAuthAttributes {

	GOOGLE("google", (attributes) -> new UserProfile(
		(String)attributes.get("email")
	)),
	NAVER("naver", (attributes) -> {
		Map<String, Object> response = (Map<String, Object>)attributes.get("response");
		return new UserProfile(
			(String)response.get("email")
		);
	});

	private final String registrationId;
	private final Function<Map<String, Object>, UserProfile> of;

	OAuthAttributes(String registrationId, Function<Map<String, Object>, UserProfile> of) {
		this.registrationId = registrationId;
		this.of = of;
	}

	public static UserProfile extract(String registrationId, Map<String, Object> attributes) {
		return Arrays.stream(values())
			.filter(provider -> registrationId.equals(provider.registrationId))
			.findFirst()
			.orElseThrow(IllegalArgumentException::new)
			.of.apply(attributes);
	}
}
