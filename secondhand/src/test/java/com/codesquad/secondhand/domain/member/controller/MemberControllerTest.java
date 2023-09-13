package com.codesquad.secondhand.domain.member.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.codesquad.secondhand.BaseControllerTest;
import com.codesquad.secondhand.annotation.ControllerIntegrationTest;
import com.codesquad.secondhand.domain.jwt.dto.request.ReissueTokenRequest;
import com.codesquad.secondhand.domain.jwt.entity.Token;
import com.codesquad.secondhand.domain.member.dto.request.RegionRequest;
import com.codesquad.secondhand.domain.member.dto.request.SignupRequest;
import com.codesquad.secondhand.domain.member.entity.Member;

@ControllerIntegrationTest
class MemberControllerTest extends BaseControllerTest {

	@Test
	@DisplayName("회원가입 요청시 회원가입이 가능하다.")
	void signUp() throws Exception {
		SignupRequest signupRequest = DummySignUpRequest();
		jwt = jwtProvider.createSignUpToken(Map.of("email", TEST_EMAIL));

		String request = objectMapper.writeValueAsString(signupRequest);

		//when & then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/members/signup")
				.header(AUTHORIZATION, JWT_TOKEN_PREFIX + jwt.getSignUpToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
			.andExpect(jsonPath("$.memberId").exists())
			.andExpect(jsonPath("$.accessToken").exists())
			.andExpect(jsonPath("$.refreshToken").exists())
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

	@Test
	@DisplayName("회원의 아이디와 지역 아이디를 입력받아 회원의 지역을 추가 할 수 있다.")
	void addRegion() throws Exception {
		// when & then
		RegionRequest requestDto = new RegionRequest(3L);
		String request = objectMapper.writeValueAsString(requestDto);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/members/1/regions")
				.header(AUTHORIZATION, JWT_TOKEN_PREFIX + jwt.getAccessToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("회원의 아이디와 지역 아이디를 입력받아 회원의 지역을 삭제 할 수 있다.")
	void deleteRegion() throws Exception {
		// when & then
		Long memberId = 1L;
		RegionRequest requestDto = new RegionRequest(4L);
		memberService.addRegion(memberId, requestDto);
		String request = objectMapper.writeValueAsString(requestDto);
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/members/1/regions")
				.header(AUTHORIZATION, JWT_TOKEN_PREFIX + jwt.getAccessToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
			.andExpect(status().isNoContent());
	}

	@Test
	@DisplayName("회원이 등록한 지역들에서 대표 지역을 설정 할 수 있다.")
	void setRegion() throws Exception {
		// when & then
		Long memberId = 1L;
		RegionRequest requestSaveDto1 = new RegionRequest(3L);
		RegionRequest requestSaveDto2 = new RegionRequest(4L);
		memberService.addRegion(memberId, requestSaveDto1);
		memberService.addRegion(memberId, requestSaveDto2);
		RegionRequest requestDto = new RegionRequest(3L);
		String request = objectMapper.writeValueAsString(requestDto);
		mockMvc.perform(MockMvcRequestBuilders.put("/api/members/1/regions")
				.header(AUTHORIZATION, JWT_TOKEN_PREFIX + jwt.getAccessToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
			.andExpect(status().isNoContent());
	}

	@Test
	@DisplayName("회원이 등록한 지역들과 대표지역을 출력한다.")
	void getSelectedRegionAndRegions() throws Exception {
		// when & then
		Long memberId = 1L;
		// 나의 지역 저장
		RegionRequest requestSaveDto1 = new RegionRequest(3L);
		RegionRequest requestSaveDto2 = new RegionRequest(4L);
		memberService.addRegion(memberId, requestSaveDto1);
		memberService.addRegion(memberId, requestSaveDto2);
		// 나의 대표 지역 설정
		RegionRequest setRegionDto = new RegionRequest(4L);
		memberService.updateSelectedRegion(memberId, setRegionDto);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/members/1/regions")
				.header(AUTHORIZATION, JWT_TOKEN_PREFIX + jwt.getAccessToken())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.selectedRegionId").exists())
			.andExpect(jsonPath("$.regions").exists())
			.andExpect(status().isOk());
	}

	private static SignupRequest DummySignUpRequest() {
		return SignupRequest.builder()
			.nickname("nickname")
			.regionsId(Arrays.asList(1L, 2L))
			.profileImg("img.png")
			.build();
	}

	@Test
	@DisplayName("사용자 정보 api 를 통해 특정 사용자의 정보를 응답으로 전송한다.")
	void getUserInfo() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/members/{memberId}", MEMBER_ID)
				.header(AUTHORIZATION, JWT_TOKEN_PREFIX + jwt.getAccessToken())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id").exists())
			.andExpect(jsonPath("$.nickname").exists())
			.andExpect(jsonPath("$.profileImg").exists())
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("토큰을 재 요청 한다.")
	void reissueToken() throws Exception {
		// given
		Token token = Token.builder()
			.member(Member.builder().id(1L).build())
			.refreshToken("123")
			.build();
		jwtQueryService.save(token);
		ReissueTokenRequest requestDto = new ReissueTokenRequest("123");
		String request = objectMapper.writeValueAsString(requestDto);
		// when & then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/oauth2/token")
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
			.andExpect(jsonPath("$.accessToken").exists())
			.andExpect(status().isOk());

	}
}
