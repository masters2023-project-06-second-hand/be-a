package com.codesquad.secondhand.domain.member.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.codesquad.secondhand.BaseControllerTest;
import com.codesquad.secondhand.annotation.IntegrationTest;
import com.codesquad.secondhand.domain.member.dto.request.SignupRequest;

@IntegrationTest
class MemberControllerTest extends BaseControllerTest {

	@Test
	@DisplayName("회원가입 요청시 회원가입이 가능하다.")
	void signUp() throws Exception {
		SignupRequest signupRequest = DummySignUpRequest();

		String request = objectMapper.writeValueAsString(signupRequest);

		//when & then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/members/signup")
				.header(AUTHORIZATION, JWT_TOKEN_PREFIX + jwt.getAccessToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("로그아웃 요청시 해당 access_token을 blackList에 등록한다.")
	void signOut() throws Exception {
		//when & then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/members/signout")
				.header(AUTHORIZATION, JWT_TOKEN_PREFIX + jwt.getAccessToken())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		Assertions.assertThat(redisUtil.hasKeyBlackList(jwt.getAccessToken())).isTrue();
	}

	private static SignupRequest DummySignUpRequest() {
		return SignupRequest.builder()
			.nickname("nickname")
			.regionsId(Arrays.asList(1L, 2L))
			.profileImg("img.png")
			.build();
	}

}
