package com.codesquad.secondhand.domain.reaction.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.domain.category.dto.response.CategoryResponse;
import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.member.service.MemberService;
import com.codesquad.secondhand.domain.product.entity.Product;
import com.codesquad.secondhand.domain.product.service.ProductService;
import com.codesquad.secondhand.domain.reaction.dto.ReactionUpdateRequest;
import com.codesquad.secondhand.domain.reaction.entity.Reaction;
import com.codesquad.secondhand.domain.reaction.repository.ReactionJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReactionService {

	private final ReactionJpaRepository reactionJpaRepository;
	private final MemberService memberService;
	private final ProductService productService;

	@Transactional
	public void update(Long productId, Long memberId, ReactionUpdateRequest reactionUpdateRequest) {
		Member member = memberService.findById(memberId);
		Product product = productService.findById(productId);

		if (reactionUpdateRequest.getIsLiked()) {
			Reaction reaction = Reaction.builder()
				.member(member)
				.product(product)
				.build();
			reactionJpaRepository.save(reaction);
			return;
		}
		reactionJpaRepository.deleteByProductAndMember(product, member);
	}

	public List<CategoryResponse> findAllOfReactedProducts(Long memberId) {
		List<Reaction> reactions = findAllByMemberId(memberId);
		return reactions.stream()
			.map(reaction -> reaction.getProduct().getCategory())
			.distinct()
			.map(category -> CategoryResponse.of(category, false))
			.collect(Collectors.toList());
	}

	private List<Reaction> findAllByMemberId(Long memberId) {
		Member member = memberService.findById(memberId);
		return reactionJpaRepository.findAllByMember(member);
	}
}
