package com.codesquad.secondhand.domain.member.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.codesquad.secondhand.annotation.IntegrationTest;
import com.codesquad.secondhand.domain.member.dto.request.SignupRequest;
import com.codesquad.secondhand.domain.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;

@IntegrationTest
class MemberControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	MemberService memberService;

	@Test
	@DisplayName("회원가입 요청시 회원가입이 가능하다.")
	void signUp() throws Exception {
		SignupRequest signupRequest = DummySignUpRequest();

		String request = objectMapper.writeValueAsString(signupRequest);

		//when & then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/members/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
			.andExpect(status().isCreated());
	}

	private static SignupRequest DummySignUpRequest() {
		return SignupRequest.builder()
			.nickname("nickname")
			.regionsId(Arrays.asList(1L, 2L))
			.profileImg("img.png")
			.build();
	}

}
