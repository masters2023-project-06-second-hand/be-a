package com.codesquad.secondhand.domain.member_region.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.member_region.entity.MemberRegion;
import com.codesquad.secondhand.domain.region.entity.Region;

public interface MemberRegionJpaRepository extends JpaRepository<MemberRegion, Long> {

	Boolean existsByMemberAndRegion(Member member, Region region);
	int countByMember(Member member);
}
