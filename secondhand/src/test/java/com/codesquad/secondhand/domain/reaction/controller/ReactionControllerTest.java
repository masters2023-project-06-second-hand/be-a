package com.codesquad.secondhand.domain.reaction.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.codesquad.secondhand.BaseControllerTest;
import com.codesquad.secondhand.annotation.IntegrationTest;
import com.codesquad.secondhand.domain.product.dto.request.ProductSaveAndUpdateRequest;
import com.codesquad.secondhand.domain.product.entity.Image;
import com.codesquad.secondhand.domain.reaction.dto.ReactionUpdateRequest;

@IntegrationTest
class ReactionControllerTest extends BaseControllerTest {

	@Test
	@DisplayName("상품 반응 API 를 통해 특정 상품에 반응을 할수 있다")
	void reactToSpecificProduct() throws Exception {
		// Given

		//1. 이미지 저장
		saveDummyImage("imageTest1");
		saveDummyImage("imageTest2");

		//2. 상품 저장
		ProductSaveAndUpdateRequest productSaveAndUpdateRequest = new ProductSaveAndUpdateRequest("상품명", 1L, 100000L,
			"상품내용", 1L,
			Arrays.asList(1L, 2L));
		Long productId = productService.save(productSaveAndUpdateRequest, MEMBER_ID);

		//3. 상품 반응 등록
		ReactionUpdateRequest reactionUpdateRequest = new ReactionUpdateRequest(true);
		String request = objectMapper.writeValueAsString(reactionUpdateRequest);

		// When & Then
		mockMvc.perform(MockMvcRequestBuilders.put("/api/products/{productId}/likes", productId)
				.header(AUTHORIZATION, JWT_TOKEN_PREFIX + jwt.getAccessToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
			.andExpect(status().isNoContent());
	}

	@Test
	@DisplayName("상품 반응 API 를 통해 특정 상품에 누른 반응을 해제할수 있다.")
	void CancelReactionOnSpecificProduct() throws Exception {
		// Given

		//1. 이미지 저장
		saveDummyImage("imageTest1");
		saveDummyImage("imageTest2");

		//2. 상품 저장
		ProductSaveAndUpdateRequest productSaveAndUpdateRequest = new ProductSaveAndUpdateRequest("상품명", 1L, 100000L,
			"상품내용", 1L,
			Arrays.asList(1L, 2L));
		Long productId = productService.save(productSaveAndUpdateRequest, MEMBER_ID);

		//3. 상품 반응 등록
		ReactionUpdateRequest react = new ReactionUpdateRequest(true);
		reactionService.update(productId, MEMBER_ID, react);

		//4. 상품 반응 취소
		ReactionUpdateRequest reactionUpdateRequest = new ReactionUpdateRequest(false);
		String request = objectMapper.writeValueAsString(reactionUpdateRequest);

		// When & Then
		mockMvc.perform(MockMvcRequestBuilders.put("/api/products/{productId}/likes", productId)
				.header(AUTHORIZATION, JWT_TOKEN_PREFIX + jwt.getAccessToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
			.andExpect(status().isNoContent());
	}

	private void saveDummyImage(String image) {
		imageJpaRepository.save(Image.builder().imgUrl(image).build());
	}
}
