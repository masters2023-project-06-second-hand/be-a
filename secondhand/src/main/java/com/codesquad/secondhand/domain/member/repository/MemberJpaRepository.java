package com.codesquad.secondhand.domain.member.repository;

import com.codesquad.secondhand.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member,Long> {
}
