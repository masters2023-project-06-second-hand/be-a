package com.codesquad.secondhand.domain.member_region.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.annotation.ServiceIntegrationTest;
import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.member.service.MemberQueryService;
import com.codesquad.secondhand.domain.member_region.entity.MemberRegion;
import com.codesquad.secondhand.domain.region.entity.Region;
import com.codesquad.secondhand.domain.region.service.RegionQueryService;
import com.codesquad.secondhand.exception.CustomRuntimeException;

@ServiceIntegrationTest
class MemberRegionQueryServiceTest {

	@Autowired
	MemberRegionQueryService memberRegionQueryService;
	@Autowired
	RegionQueryService regionQueryService;
	@Autowired
	MemberQueryService memberQueryService;

	@DisplayName("member 객체와 region객체를담은 List를 이용해 memberRegion 객체를 만들고 리스트의 사이즈 만큼 일괄적으로 DB에 저장 할 수 있다. ")
	@Test
	@Transactional
	void saveAll() {
		// given
		Member member = memberQueryService.findById(1L);
		List<Region> regions = regionQueryService.findByIds(List.of(2L, 3L));
		List<MemberRegion> memberRegions = MemberRegion.of(member, regions);

		// when
		memberRegionQueryService.saveAll(memberRegions);

		// then
		MemberRegion memberRegion1 = memberRegionQueryService.findByMemberAndRegion(member, regions.get(0));
		MemberRegion memberRegion2 = memberRegionQueryService.findByMemberAndRegion(member, regions.get(1));

		assertThat(memberRegion1.getMember().getId()).isEqualTo(member.getId());
		assertThat(memberRegion1.getRegion().getName()).isEqualTo(regions.get(0).getName());
		assertThat(memberRegion2.getMember().getId()).isEqualTo(member.getId());
		assertThat(memberRegion2.getRegion().getName()).isEqualTo(regions.get(1).getName());
	}

	@DisplayName("member 객체와 region객체를 이용해 meberRegion 객체를 생성하고 디비에 저장한다. (saveAll은 회원가입 할 때, save는 지역추가 텝에서 추가 할때 사용) ")
	@Test
	@Transactional
	void save() {
		// given
		Member member = memberQueryService.findById(1L);
		Region region = regionQueryService.findById(2L);
		MemberRegion memberRegion = MemberRegion.of(member, region);

		// when
		memberRegionQueryService.save(memberRegion);

		// then
		List<MemberRegion> memberRegions = memberRegionQueryService.findAllMemberRegion(memberRegion.getId());
		assertThat(memberRegions.get(0).getMember().getId()).isEqualTo(member.getId());
		assertThat(memberRegions.get(0).getRegion().getName()).isEqualTo(region.getName());
	}

	@DisplayName("member 객체를 통해 회원의 지역 정보를 담은 MemberRegion 의 갯수를 count 한다. ")
	@Test
	void countByMember() {
		// given
		Member member = memberQueryService.findById(1L);
		int firstCount = memberRegionQueryService.countByMember(member);
		Region region = regionQueryService.findById(1L);
		MemberRegion memberRegion = MemberRegion.of(member, region);
		memberRegionQueryService.save(memberRegion);

		// when
		int resultCount = memberRegionQueryService.countByMember(member);

		// then
		assertThat(firstCount).isEqualTo(0);
		assertThat(resultCount).isEqualTo(1);
	}

	@DisplayName("member와 region을 이용해 memberRegion을 조회하고 DB에 memberRegion이 없으면 false 반환")
	@Test
	void existsByMemberAndRegionFailed() {
		// given
		Member member = memberQueryService.findById(1L);
		Region region = regionQueryService.findById(1L);

		// when
		Boolean actual = memberRegionQueryService.existsByMemberAndRegion(member, region);
		// then
		assertThat(actual).isFalse();
	}

	@DisplayName("member와 region을 이용해 memberRegion을 조회하고 DB에 memberRegion이 있으면 true 반환")
	@Test
	void existsByMemberAndRegionSuccess() {
		// given
		Member member = memberQueryService.findById(1L);
		Region region = regionQueryService.findById(1L);
		MemberRegion memberRegion = MemberRegion.of(member, region);
		memberRegionQueryService.save(memberRegion);

		// when
		Boolean actual = memberRegionQueryService.existsByMemberAndRegion(member, region);

		// then
		assertThat(actual).isTrue();
	}

	@DisplayName("memberId 를 통해 유저의 등록 지역정보를 모두 조회 한다.")
	@Test
	void findAllMemberRegion() {
		// given
		Member member = memberQueryService.findById(1L);
		List<Region> regions = regionQueryService.findByIds(List.of(1L, 2L));
		List<MemberRegion> memberRegions = MemberRegion.of(member, regions);
		memberRegionQueryService.saveAll(memberRegions);

		// when
		List<MemberRegion> actualMemberRegions = memberRegionQueryService.findAllMemberRegion(member.getId());

		// then
		assertThat(actualMemberRegions.get(0).getRegion().getId()).isEqualTo(memberRegions.get(0).getRegion().getId());
		assertThat(actualMemberRegions.get(1).getMember().getId()).isEqualTo(memberRegions.get(1).getMember().getId());
	}

	@DisplayName("해당 member 객체와 해당 region 객체를 동시에 가지는 memberRegion 객체를 조회한다.")
	@Test
	void findByMemberAndRegionSuccess() {
		// given
		Member member = memberQueryService.findById(1L);
		Region region = regionQueryService.findById(1L);
		MemberRegion memberRegion = MemberRegion.of(member, region);
		memberRegionQueryService.save(memberRegion);

		// when
		MemberRegion actualMemberRegion = memberRegionQueryService.findByMemberAndRegion(member, region);

		// then
		assertThat(actualMemberRegion.getMember().getId()).isEqualTo(member.getId());
		assertThat(actualMemberRegion.getRegion().getId()).isEqualTo(region.getId());
	}

	@DisplayName("해당 member 객체와 해당 region 객체를 동시에 가지는 memberRegion 객체를 조회할 때 member나 지역이 하나라도 다르면 예외가 발생한다.")
	@Test
	void findByMemberAndRegionFailed() {
		// given
		Member savedMember = memberQueryService.findById(1L);
		Region savedRegion = regionQueryService.findById(1L);
		MemberRegion memberRegion = MemberRegion.of(savedMember, savedRegion);
		memberRegionQueryService.save(memberRegion);
		Member notSavedMember = memberQueryService.findById(2L);
		Region notSavedRegion = regionQueryService.findById(2L);

		// when & then
		assertThatThrownBy(
			() -> memberRegionQueryService.findByMemberAndRegion(notSavedMember, savedRegion)).isInstanceOf(
			CustomRuntimeException.class);
		assertThatThrownBy(
			() -> memberRegionQueryService.findByMemberAndRegion(savedMember, notSavedRegion)).isInstanceOf(
			CustomRuntimeException.class);
	}

	@DisplayName("memberRegion 객체로 등록 지역 정보를 삭제한다.")
	@Test
	void delete() {
		// given
		Member member = memberQueryService.findById(1L);
		Region region = regionQueryService.findById(1L);
		MemberRegion memberRegion = MemberRegion.of(member, region);
		memberRegionQueryService.save(memberRegion);
		List<MemberRegion> memberRegions = memberRegionQueryService.findAllMemberRegion(member.getId());

		// when
		memberRegionQueryService.delete(memberRegion);

		// then
		List<MemberRegion> afterDeleteMemberRegions = memberRegionQueryService.findAllMemberRegion(member.getId());
		assertThat(afterDeleteMemberRegions.size()).isEqualTo(memberRegions.size() - 1);

	}
}
