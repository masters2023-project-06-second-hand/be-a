package com.codesquad.secondhand.domain.jwt.dto.response;

import com.codesquad.secondhand.domain.jwt.entity.Token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ReissueTokenResponse {
	private String accessToken;
	private String refreshToken;
	private Long memberId;

	public static ReissueTokenResponse of(Token token, String reissuedAccessToken) {
		return ReissueTokenResponse.builder()
			.accessToken(reissuedAccessToken)
			.refreshToken(token.getRefreshToken())
			.memberId(token.getMember().getId())
			.build();
	}
}
