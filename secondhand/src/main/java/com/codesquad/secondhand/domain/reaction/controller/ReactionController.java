package com.codesquad.secondhand.domain.reaction.controller;

import static com.codesquad.secondhand.common.util.RequestParser.*;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codesquad.secondhand.domain.category.dto.response.CategoryResponse;
import com.codesquad.secondhand.domain.reaction.dto.ReactionUpdateRequest;
import com.codesquad.secondhand.domain.reaction.service.ReactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReactionController {

	private final ReactionService reactionService;

	@PutMapping("/products/{productId}/likes")
	public ResponseEntity update(@PathVariable Long productId, @RequestBody ReactionUpdateRequest reactionUpdateRequest,
		HttpServletRequest request) {
		Long memberId = extractMemberId(request);
		reactionService.update(productId, memberId, reactionUpdateRequest);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/members/{memberId}/likes/categories")
	public ResponseEntity<List<CategoryResponse>> findAllOfReactedProducts(@PathVariable Long memberId) {
		List<CategoryResponse> categories = reactionService.findAllOfReactedProducts(memberId);
		return ResponseEntity.ok().body(categories);
	}
}
