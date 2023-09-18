package com.codesquad.secondhand.domain.member_region.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.codesquad.secondhand.annotation.ServiceIntegrationTest;
import com.codesquad.secondhand.domain.member.dto.request.RegionRequest;
import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.member.service.MemberQueryService;
import com.codesquad.secondhand.domain.region.service.RegionQueryService;
import com.codesquad.secondhand.exception.CustomRuntimeException;

@ServiceIntegrationTest
class MemberRegionServiceTest {

	@Autowired
	MemberRegionService memberRegionService;
	@Autowired
	MemberQueryService memberQueryService;
	@Autowired
	RegionQueryService regionQueryService;

	@DisplayName("등록 지역을 2개 초과해서 선택하면 예외발생")
	@Test
	void addRegionFailedWithSize() {
		// given
		Member member = memberQueryService.findById(1L);
		RegionRequest region1 = new RegionRequest(1L);
		RegionRequest region2 = new RegionRequest(2L);
		RegionRequest region3 = new RegionRequest(3L);
		memberRegionService.addRegion(member.getId(), region1);
		memberRegionService.addRegion(member.getId(), region2);

		// when & then
		assertThatThrownBy(() -> memberRegionService.addRegion(member.getId(), region3)).isInstanceOf(
			CustomRuntimeException.class);
	}

	@DisplayName("등록 지역을 2개 초과해서 선택하면 예외발생")
	@Test
	void addRegionFailedWithDuplicated() {
		// given
		Member member = memberQueryService.findById(1L);
		RegionRequest region1 = new RegionRequest(1L);
		RegionRequest region2 = new RegionRequest(2L);
		RegionRequest region3 = new RegionRequest(3L);
		memberRegionService.addRegion(member.getId(), region1);
		memberRegionService.addRegion(member.getId(), region2);

		// when & then
		assertThatThrownBy(() -> memberRegionService.addRegion(member.getId(), region3)).isInstanceOf(
			CustomRuntimeException.class);
	}

	@DisplayName("지역을 삭제하고, 지역목록이 남아있다면 남아있는 지역을 지역 목록으로 설정 ")
	@Test
	void deleteAndUpdateSelectedRegion() {
		// given
		Member member = memberQueryService.findById(1L);
		RegionRequest region1 = new RegionRequest(1L);
		RegionRequest region2 = new RegionRequest(2L);
		memberRegionService.addRegion(member.getId(), region1);
		memberRegionService.addRegion(member.getId(), region2);
		memberRegionService.updateSelectedRegion(member.getId(), region1);

		// when
		memberRegionService.deleteRegion(member.getId(), region1);

		// then
		Member afterDeleteMember = memberQueryService.findById(1L);
		assertThat(memberRegionService.getRegion(1L).getRegions().size()).isEqualTo(1);
		assertThat(afterDeleteMember.getSelectedRegion()).isEqualTo(region2.getId());
	}

	@DisplayName("지역 목록이 하나이면 삭제시 예외 발생 ")
	@Test
	void deleteAndUpdateSelectedRegionFailed() {
		// given
		Member member = memberQueryService.findById(1L);
		RegionRequest region1 = new RegionRequest(1L);
		memberRegionService.addRegion(member.getId(), region1);

		// when & then
		assertThatThrownBy(() -> memberRegionService.deleteRegion(member.getId(), region1)).isInstanceOf(
			CustomRuntimeException.class);
	}
}
