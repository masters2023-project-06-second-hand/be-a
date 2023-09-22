package com.codesquad.secondhand.domain.reaction.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.domain.category.dto.response.CategoryResponse;
import com.codesquad.secondhand.domain.chat.service.ChatQueryService;
import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.member.service.MemberQueryService;
import com.codesquad.secondhand.domain.product.dto.response.ProductResponse;
import com.codesquad.secondhand.domain.product.entity.Product;
import com.codesquad.secondhand.domain.product.service.ProductQueryService;
import com.codesquad.secondhand.domain.reaction.dto.ReactionUpdateRequest;
import com.codesquad.secondhand.domain.reaction.entity.Reaction;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReactionService {

	private final ReactionQueryService reactionQueryService;
	private final MemberQueryService memberQueryService;
	private final ProductQueryService productQueryService;
	private final ChatQueryService chatQueryService;

	@Transactional
	public void update(Long productId, Long memberId, ReactionUpdateRequest reactionUpdateRequest) {
		Member member = memberQueryService.findById(memberId);
		Product product = productQueryService.findById(productId);

		if (reactionUpdateRequest.isLiked()) {
			Reaction reaction = Reaction.of(product, member);
			reactionQueryService.save(reaction);
			return;
		}
		reactionQueryService.deleteByProductAndMember(product, member);
	}

	public List<CategoryResponse> findAllOfReactedCategories(Long memberId) {
		List<Reaction> reactions = findAllByMemberId(memberId);
		return reactions.stream()
			.map(reaction -> reaction.getProduct().getCategory())
			.distinct()
			.map(category -> CategoryResponse.of(category, false))
			.collect(Collectors.toUnmodifiableList());
	}

	private List<Reaction> findAllByMemberId(Long memberId) {
		Member member = memberQueryService.findById(memberId);
		return reactionQueryService.findAllByMember(member);
	}

	public List<ProductResponse> findAllOfReactedProducts(Long memberId, Long categoryId) {
		List<Reaction> reactions = findAllByMemberId(memberId);
		Stream<Reaction> reactionStream = reactions.stream();

		if (categoryId != null) {
			reactionStream = reactionStream.filter(
				reaction -> reaction.isProductInCategory(categoryId));
		}

		return reactionStream.map(this::mapToProductResponse)
			.collect(Collectors.toUnmodifiableList());
	}

	private ProductResponse mapToProductResponse(Reaction reaction) {
		Product product = reaction.getProduct();
		Long reactionCount = reactionQueryService.countByProduct(product);
		Long chattingCount = chatQueryService.countByProduct(product);
		return ProductResponse.of(product, reactionCount, chattingCount);
	}
}
