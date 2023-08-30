package com.codesquad.secondhand.domain.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Jwt {

	private final String accessToken;
	private final String refreshToken;

}
