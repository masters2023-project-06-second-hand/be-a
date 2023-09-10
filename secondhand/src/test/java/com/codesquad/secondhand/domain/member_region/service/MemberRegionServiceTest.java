package com.codesquad.secondhand.domain.member_region.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.codesquad.secondhand.annotation.ServiceIntegrationTest;
import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.member.service.MemberQueryService;
import com.codesquad.secondhand.domain.member_region.entity.MemberRegion;
import com.codesquad.secondhand.domain.region.entity.Region;
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
	void saveFailedWithSize() {
		// given
		Member member = memberQueryService.findById(1L);
		Region region1 = regionQueryService.findById(1L);
		Region region2 = regionQueryService.findById(2L);
		Region region3 = regionQueryService.findById(3L);
		MemberRegion memberRegion1 = MemberRegion.of(member, region1);
		MemberRegion memberRegion2 = MemberRegion.of(member, region2);
		MemberRegion memberRegion3 = MemberRegion.of(member, region3);
		memberRegionService.save(memberRegion1);
		memberRegionService.save(memberRegion2);
		// when & then
		assertThatThrownBy(() -> memberRegionService.save(memberRegion3)).isInstanceOf(CustomRuntimeException.class);
	}

	@DisplayName("등록 지역을 2개 초과해서 선택하면 예외발생")
	@Test
	void saveFailedWithDuplicated() {
		// given
		Member member = memberQueryService.findById(1L);
		Region region1 = regionQueryService.findById(1L);
		Region region2 = regionQueryService.findById(1L);
		MemberRegion memberRegion1 = MemberRegion.of(member, region1);
		MemberRegion memberRegion2 = MemberRegion.of(member, region2);
		memberRegionService.save(memberRegion1);
		// when & then
		assertThatThrownBy(() -> memberRegionService.save(memberRegion2)).isInstanceOf(CustomRuntimeException.class);
	}
}
