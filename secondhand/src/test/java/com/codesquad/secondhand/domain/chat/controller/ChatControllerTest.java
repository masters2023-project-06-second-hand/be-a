package com.codesquad.secondhand.domain.chat.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.codesquad.secondhand.BaseControllerTest;
import com.codesquad.secondhand.annotation.ControllerIntegrationTest;
import com.codesquad.secondhand.domain.chat.dto.request.ChatRequest;
import com.codesquad.secondhand.domain.jwt.domain.Jwt;
import com.codesquad.secondhand.domain.product.dto.request.ProductSaveAndUpdateRequest;
import com.codesquad.secondhand.domain.product.entity.Image;

@ControllerIntegrationTest
class ChatControllerTest extends BaseControllerTest {

	private static final Long PURCHASER_ID = 2L;

	@Test
	@DisplayName("채팅방 조회 api 를 통해 채팅방을 생성할수 있다.")
	void getChatRoomWhenChatRoomNotExists() throws Exception {
		// given

		//1. memberId 가 2인 accessToken 생성
		Jwt purchaserJwtToken = jwtProvider.createTokens(Map.of("memberId", PURCHASER_ID));

		//2. product 저장 (채팅방을 생성하려면 특정 물품이 있어야함)
		Long savedProductId = saveProduct();

		//3. ChatRequest 생성
		ChatRequest chatRequest = ChatRequest.builder()
			.productId(savedProductId)
			.sellerId(MEMBER_ID)
			.build();
		String requestJson = objectMapper.writeValueAsString(chatRequest);

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/chats/room-id")
				.header(AUTHORIZATION, JWT_TOKEN_PREFIX + purchaserJwtToken.getAccessToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.chatRoomId").exists());
	}

	@Test
	@DisplayName("채팅방 조회 api 를 통해 만들어진 채팅방을 조회 할수 있다.")
	void getChatRoomWhenChatRoomAlreadyExists() throws Exception {
		// given

		//1. memberId 가 2인 accessToken 생성
		Jwt purchaserJwtToken = jwtProvider.createTokens(Map.of("memberId", PURCHASER_ID));

		//2. product 저장 (채팅방을 생성하려면 특정 물품이 있어야함)
		Long savedProductId = saveProduct();

		//3. ChatRequest 생성
		ChatRequest chatRequest = ChatRequest.builder()
			.productId(savedProductId)
			.sellerId(MEMBER_ID)
			.build();
		String requestJson = objectMapper.writeValueAsString(chatRequest);

		//4. chatRoom 생성
		Long expected = chatService.getChatRoom(chatRequest, PURCHASER_ID);

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/chats/room-id")
				.header(AUTHORIZATION, JWT_TOKEN_PREFIX + purchaserJwtToken.getAccessToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.chatRoomId").value(expected));
	}

	@Test
	@DisplayName("채팅방 들어가기 api를 통해 특정 채팅방에 필요한 모든 데이터를 응답에 포함한다.")
	void getChatDetails() throws Exception {
		// given

		//1. memberId 가 2인 accessToken 생성
		Jwt purchaserJwtToken = jwtProvider.createTokens(Map.of("memberId", PURCHASER_ID));

		//2. product 저장 (채팅방을 생성하려면 특정 물품이 있어야함)
		Long savedProductId = saveProduct();

		//3. ChatRequest 생성
		ChatRequest chatRequest = ChatRequest.builder()
			.productId(savedProductId)
			.sellerId(MEMBER_ID)
			.build();
		String requestJson = objectMapper.writeValueAsString(chatRequest);

		//4. chatRoom 생성
		Long chatRoomId = chatService.getChatRoom(chatRequest, PURCHASER_ID);

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/chats/{chatRoomId}", chatRoomId)
				.header(AUTHORIZATION, JWT_TOKEN_PREFIX + purchaserJwtToken.getAccessToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.product.name").exists())
			.andExpect(jsonPath("$.product.price").exists())
			.andExpect(jsonPath("$.product.thumbnailUrl").exists())
			.andExpect(jsonPath("$.opponentName").exists())
			.andExpect(jsonPath("$.chatRoomId").exists())
			.andExpect(jsonPath("$.messages").isEmpty());
	}

	private Long saveProduct() {
		//2. 이미지 저장
		saveDummyImage("imageTest1");
		saveDummyImage("imageTest2");

		//3. 상품 저장
		ProductSaveAndUpdateRequest productRequestCat1Reg1 = new ProductSaveAndUpdateRequest("상품명", 1L, 100000L, "상품내용",
			1L, Arrays.asList(1L, 2L));
		Long savedProductId = productService.save(productRequestCat1Reg1, MEMBER_ID);
		return savedProductId;
	}

	private void saveDummyImage(String image) {
		imageQueryService.save(Image.builder().imgUrl(image).build());
	}
}
