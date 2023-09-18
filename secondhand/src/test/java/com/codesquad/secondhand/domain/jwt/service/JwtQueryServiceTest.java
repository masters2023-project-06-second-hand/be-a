package com.codesquad.secondhand.domain.jwt.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.annotation.ServiceIntegrationTest;
import com.codesquad.secondhand.domain.jwt.entity.Token;
import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.exception.CustomRuntimeException;

@ServiceIntegrationTest
class JwtQueryServiceTest {

	@Autowired
	JwtQueryService jwtQueryService;

	@DisplayName("토큰을 DB에서 삭제 한다. : 삭제 되어 해당 토큰을 찾을 수 없어 예외 발생")
	@Test
	@Transactional
	void delete() {
		// given
		Token token = Token.builder()
			.member(Member.builder().id(1L).build())
			.refreshToken("123")
			.build();
		jwtQueryService.save(token);

		// when
		jwtQueryService.delete(token.getMember().getId());

		// then
		assertThatThrownBy(() -> jwtQueryService.findByRefreshToken("123")).isInstanceOf(
			CustomRuntimeException.class);
	}

	@DisplayName("토큰을 DB 에 저장한다. : 저장 후 조회가 가능하다.")
	@Test
	void save() {
		// given
		Token token = Token.builder()
			.member(Member.builder().id(1L).build())
			.refreshToken("123")
			.build();

		// when
		jwtQueryService.save(token);

		// then
		Token findToken = jwtQueryService.findByRefreshToken("123");
		assertThat(findToken.getMember().getId()).isEqualTo(token.getMember().getId());
	}
}
