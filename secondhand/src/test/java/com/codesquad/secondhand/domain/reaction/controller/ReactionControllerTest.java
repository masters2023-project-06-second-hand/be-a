package com.codesquad.secondhand.domain.reaction.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.assertj.core.api.Assertions;
import org.json.JSONArray;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.codesquad.secondhand.BaseControllerTest;
import com.codesquad.secondhand.annotation.ControllerIntegrationTest;
import com.codesquad.secondhand.domain.product.dto.request.ProductSaveAndUpdateRequest;
import com.codesquad.secondhand.domain.product.entity.Image;
import com.codesquad.secondhand.domain.reaction.dto.ReactionUpdateRequest;

@ControllerIntegrationTest
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
		imageQueryService.save(Image.builder().imgUrl(image).build());
	}

	@Test
	@DisplayName("사용자가 좋아요를 누른 상품들의 카테고리 목록을 응답에 전송한다.")
	void findAllCategoryOfReactedProducts() throws Exception {
		//1. 이미지 저장
		saveDummyImage("imageTest1");
		saveDummyImage("imageTest2");
		saveDummyImage("imageTest3");
		saveDummyImage("imageTest4");

		//2.1 첫번째 상품 저장
		ProductSaveAndUpdateRequest productSaveAndUpdateRequest = new ProductSaveAndUpdateRequest("상품명", 1L, 100000L,
			"상품내용", 1L,
			Arrays.asList(1L, 2L));
		Long productId = productService.save(productSaveAndUpdateRequest, MEMBER_ID);

		//2.2 두번째 상품 저장
		ProductSaveAndUpdateRequest secondProductSaveAndUpdateRequest = new ProductSaveAndUpdateRequest("상품명", 2L,
			100000L,
			"상품내용", 1L,
			Arrays.asList(3L, 4L));
		Long secondProductId = productService.save(secondProductSaveAndUpdateRequest, MEMBER_ID);

		//3.1 첫번째 상품 반응 등록
		ReactionUpdateRequest react = new ReactionUpdateRequest(true);
		reactionService.update(productId, MEMBER_ID, react);

		//3.2 두번째 상품 반응 등록
		reactionService.update(secondProductId, MEMBER_ID, react);

		//4. 좋아요를 누른 카테고리 목록 조회
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.get("/api/members/{memberId}/likes/categories", MEMBER_ID)
					.header(AUTHORIZATION, JWT_TOKEN_PREFIX + jwt.getAccessToken())
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id").exists())
			.andExpect(jsonPath("$[0].name").exists())
			.andExpect(jsonPath("$[0].imgUrl").doesNotExist())
			.andExpect(jsonPath("$[1].id").exists())
			.andExpect(jsonPath("$[1].name").exists())
			.andExpect(jsonPath("$[1].imgUrl").doesNotExist())
			.andReturn();

		String responseBody = result.getResponse().getContentAsString();
		JSONArray jsonArray = new JSONArray(responseBody);
		Assertions.assertThat(jsonArray.length()).isEqualTo(2);
	}

	@Test
	@DisplayName("사용자가 좋아요를 누른 상품들의 목록을 응답에 전송한다.")
	void findAllProductOfReactedProducts() throws Exception {
		//1. 이미지 저장
		saveDummyImage("imageTest1");
		saveDummyImage("imageTest2");
		saveDummyImage("imageTest3");
		saveDummyImage("imageTest4");

		//2.1 첫번째 상품 저장
		ProductSaveAndUpdateRequest productSaveAndUpdateRequest = new ProductSaveAndUpdateRequest("상품명", 1L, 100000L,
			"상품내용", 1L,
			Arrays.asList(1L, 2L));
		Long productId = productService.save(productSaveAndUpdateRequest, MEMBER_ID);

		//2.2 두번째 상품 저장
		ProductSaveAndUpdateRequest secondProductSaveAndUpdateRequest = new ProductSaveAndUpdateRequest("상품명", 2L,
			100000L,
			"상품내용", 1L,
			Arrays.asList(3L, 4L));
		Long secondProductId = productService.save(secondProductSaveAndUpdateRequest, MEMBER_ID);

		//3.1 첫번째 상품 반응 등록
		ReactionUpdateRequest react = new ReactionUpdateRequest(true);
		reactionService.update(productId, MEMBER_ID, react);

		//3.2 두번째 상품 반응 등록
		reactionService.update(secondProductId, MEMBER_ID, react);

		//4. 좋아요를 누른 카테고리 목록 조회
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.get("/api/members/{memberId}/likes", MEMBER_ID)
					.header(AUTHORIZATION, JWT_TOKEN_PREFIX + jwt.getAccessToken())
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id").exists())
			.andExpect(jsonPath("$[0].writerId").exists())
			.andExpect(jsonPath("$[0].thumbnailUrl").exists())
			.andExpect(jsonPath("$[0].name").exists())
			.andExpect(jsonPath("$[0].region").exists())
			.andExpect(jsonPath("$[0].createdAt").exists())
			.andExpect(jsonPath("$[0].status").exists())
			.andExpect(jsonPath("$[0].price").exists())
			.andExpect(jsonPath("$[0].likeCount").exists())
			.andExpect(jsonPath("$[0].chattingCount").exists())
			.andReturn();

		String responseBody = result.getResponse().getContentAsString();
		JSONArray jsonArray = new JSONArray(responseBody);
		Assertions.assertThat(jsonArray.length()).isEqualTo(2);
	}

	@Test
	@DisplayName("사용자가 좋아요를 누른 상품들의 목록중 특정 카테고리 목록에 해당하는 상품만을 응답을 통해 전송한다.")
	void findAllProductOfReactedProductsFilteredByCategoryId() throws Exception {
		//1. 이미지 저장
		saveDummyImage("imageTest1");
		saveDummyImage("imageTest2");
		saveDummyImage("imageTest3");
		saveDummyImage("imageTest4");

		//2.1 첫번째 상품 저장
		ProductSaveAndUpdateRequest productSaveAndUpdateRequest = new ProductSaveAndUpdateRequest("상품명", 1L, 100000L,
			"상품내용", 1L,
			Arrays.asList(1L, 2L));
		Long productId = productService.save(productSaveAndUpdateRequest, MEMBER_ID);

		//2.2 두번째 상품 저장
		ProductSaveAndUpdateRequest secondProductSaveAndUpdateRequest = new ProductSaveAndUpdateRequest("상품명", 2L,
			100000L,
			"상품내용", 1L,
			Arrays.asList(3L, 4L));
		Long secondProductId = productService.save(secondProductSaveAndUpdateRequest, MEMBER_ID);

		//3.1 첫번째 상품 반응 등록
		ReactionUpdateRequest react = new ReactionUpdateRequest(true);
		reactionService.update(productId, MEMBER_ID, react);

		//3.2 두번째 상품 반응 등록
		reactionService.update(secondProductId, MEMBER_ID, react);

		//4. 좋아요를 누른 카테고리 목록 조회
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.get("/api/members/{memberId}/likes", MEMBER_ID)
					.header(AUTHORIZATION, JWT_TOKEN_PREFIX + jwt.getAccessToken())
					.param("categoryId", "1")
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id").exists())
			.andExpect(jsonPath("$[0].writerId").exists())
			.andExpect(jsonPath("$[0].thumbnailUrl").exists())
			.andExpect(jsonPath("$[0].name").exists())
			.andExpect(jsonPath("$[0].region").exists())
			.andExpect(jsonPath("$[0].createdAt").exists())
			.andExpect(jsonPath("$[0].status").exists())
			.andExpect(jsonPath("$[0].price").exists())
			.andExpect(jsonPath("$[0].likeCount").exists())
			.andExpect(jsonPath("$[0].chattingCount").exists())
			.andReturn();

		String responseBody = result.getResponse().getContentAsString();
		JSONArray jsonArray = new JSONArray(responseBody);
		Assertions.assertThat(jsonArray.length()).isEqualTo(1);
	}

}
