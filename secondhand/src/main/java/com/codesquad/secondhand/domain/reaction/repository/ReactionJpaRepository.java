package com.codesquad.secondhand.domain.reaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.product.entity.Product;
import com.codesquad.secondhand.domain.reaction.entity.Reaction;

public interface ReactionJpaRepository extends JpaRepository<Reaction, Long> {

	void deleteByProductAndMember(Product product, Member member);
}
