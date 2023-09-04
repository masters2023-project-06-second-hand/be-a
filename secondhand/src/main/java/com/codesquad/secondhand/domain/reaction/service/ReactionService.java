package com.codesquad.secondhand.domain.reaction.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.domain.category.dto.response.CategoryResponse;
import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.member.service.MemberService;
import com.codesquad.secondhand.domain.product.dto.response.ProductFindAllResponse;
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

	public List<CategoryResponse> findAllOfReactedCategories(Long memberId) {
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

	// 이거 findAll 메서드랑 형태가 매우 비슷함 파라미터는 다르고 어떻게 못할까?
	// 위치도 여기가 맞는지 확인해봐야할듯
	public List<ProductFindAllResponse> findAllOfReactedProducts(Long memberId, Long categoryId) {
		List<Reaction> reactions = findAllByMemberId(memberId);
		Stream<Reaction> reactionStream = reactions.stream();

		// 이거 getProduct 부분 너무 더러운데 디미터 법칙 준수해야할듯
		if (categoryId != null) {
			reactionStream = reactionStream.filter(
				reaction -> reaction.getProduct().getCategory().getId().equals(categoryId));
		}

		return reactionStream.map(this::mapToProductFindAllResponse)
			.collect(Collectors.toUnmodifiableList());
	}

	private ProductFindAllResponse mapToProductFindAllResponse(Reaction reaction) {
		Product product = reaction.getProduct();
		long reactionCount = reactionJpaRepository.countByProduct(product);
		return ProductFindAllResponse.of(product, reactionCount);
	}
}
