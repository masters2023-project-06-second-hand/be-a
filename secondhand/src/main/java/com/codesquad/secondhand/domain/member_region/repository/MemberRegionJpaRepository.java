package com.codesquad.secondhand.domain.member_region.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codesquad.secondhand.domain.member_region.entity.MemberRegion;

public interface MemberRegionJpaRepository extends JpaRepository<MemberRegion, Long> {
}
