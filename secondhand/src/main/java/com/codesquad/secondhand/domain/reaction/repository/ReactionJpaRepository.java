package com.codesquad.secondhand.domain.reaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.product.entity.Product;
import com.codesquad.secondhand.domain.reaction.entity.Reaction;

public interface ReactionJpaRepository extends JpaRepository<Reaction, Long> {

	void deleteByProductAndMember(Product product, Member member);

	List<Reaction> findAllByMember(Member member);

	long countByProduct(Product product);

	Boolean existsByMemberIdAndProductId(Long memberId, Long id);
}
