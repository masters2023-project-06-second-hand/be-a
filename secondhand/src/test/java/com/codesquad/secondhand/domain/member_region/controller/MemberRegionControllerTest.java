package com.codesquad.secondhand.domain.member_region.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.codesquad.secondhand.BaseControllerTest;
import com.codesquad.secondhand.annotation.ControllerIntegrationTest;
import com.codesquad.secondhand.domain.member.dto.request.RegionRequest;

@ControllerIntegrationTest
class MemberRegionControllerTest extends BaseControllerTest {

	@Test
	@DisplayName("회원의 아이디와 지역 아이디를 입력받아 회원의 지역을 추가 할 수 있다.")
	void addRegion() throws Exception {
		//given
		RegionRequest requestDto = new RegionRequest(3L);
		String request = objectMapper.writeValueAsString(requestDto);

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/members/1/regions")
				.header(AUTHORIZATION, JWT_TOKEN_PREFIX + jwt.getAccessToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("회원의 아이디와 지역 아이디를 입력받아 회원의 지역을 삭제 할 수 있다.")
	void deleteRegion() throws Exception {
		//given
		Long memberId = 1L;

		RegionRequest requestSaveDto1 = new RegionRequest(3L);
		RegionRequest requestSaveDto2 = new RegionRequest(4L);
		memberRegionService.addRegion(memberId, requestSaveDto1);
		memberRegionService.addRegion(memberId, requestSaveDto2);

		RegionRequest requestDto = new RegionRequest(3L);
		String request = objectMapper.writeValueAsString(requestDto);

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/members/1/regions")
				.header(AUTHORIZATION, JWT_TOKEN_PREFIX + jwt.getAccessToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
			.andExpect(status().isNoContent());
	}

	@Test
	@DisplayName("회원이 등록한 지역들에서 대표 지역을 설정 할 수 있다.")
	void setRegion() throws Exception {
		//given
		Long memberId = 1L;

		RegionRequest requestSaveDto1 = new RegionRequest(3L);
		RegionRequest requestSaveDto2 = new RegionRequest(4L);
		memberRegionService.addRegion(memberId, requestSaveDto1);
		memberRegionService.addRegion(memberId, requestSaveDto2);

		RegionRequest requestDto = new RegionRequest(3L);
		String request = objectMapper.writeValueAsString(requestDto);

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.put("/api/members/1/regions")
				.header(AUTHORIZATION, JWT_TOKEN_PREFIX + jwt.getAccessToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
			.andExpect(status().isNoContent());
	}

	@Test
	@DisplayName("회원이 등록한 지역들과 대표지역을 출력한다.")
	void getSelectedRegionAndRegions() throws Exception {
		// given
		Long memberId = 1L;

		// 나의 지역 저장
		RegionRequest requestSaveDto1 = new RegionRequest(3L);
		RegionRequest requestSaveDto2 = new RegionRequest(4L);
		memberRegionService.addRegion(memberId, requestSaveDto1);
		memberRegionService.addRegion(memberId, requestSaveDto2);

		// 나의 대표 지역 설정
		RegionRequest setRegionDto = new RegionRequest(4L);
		memberRegionService.updateSelectedRegion(memberId, setRegionDto);

		//when & then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/members/1/regions")
				.header(AUTHORIZATION, JWT_TOKEN_PREFIX + jwt.getAccessToken())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.selectedRegionId").exists())
			.andExpect(jsonPath("$.regions").exists())
			.andExpect(status().isOk());
	}

}
