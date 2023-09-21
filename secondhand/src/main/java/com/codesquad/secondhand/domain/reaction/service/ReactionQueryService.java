package com.codesquad.secondhand.domain.reaction.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.product.entity.Product;
import com.codesquad.secondhand.domain.reaction.entity.Reaction;
import com.codesquad.secondhand.domain.reaction.repository.ReactionJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReactionQueryService {

	private final ReactionJpaRepository reactionJpaRepository;

	@Transactional
	public void save(Reaction reaction) {
		reactionJpaRepository.save(reaction);
	}

	@Transactional
	public void deleteByProductAndMember(Product product, Member member) {
		reactionJpaRepository.deleteByProductAndMember(product, member);
	}

	public List<Reaction> findAllByMember(Member member) {
		return reactionJpaRepository.findAllByMember(member);
	}

	public long countByProduct(Product product) {
		return reactionJpaRepository.countByProduct(product);
	}

	public Boolean isLiked(Long memberId, Product product) {
		return reactionJpaRepository.existsByMemberIdAndProductId(memberId, product.getId());
	}
}
