package com.codesquad.secondhand.domain.product.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.codesquad.secondhand.annotation.IntegrationTest;
import com.codesquad.secondhand.domain.product.dto.request.ProductSaveAndUpdateRequest;
import com.codesquad.secondhand.domain.product.dto.request.ProductUpdateRequest;
import com.codesquad.secondhand.domain.product.entity.Image;
import com.codesquad.secondhand.domain.product.repository.ImageJpaRepository;
import com.codesquad.secondhand.domain.product.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

@IntegrationTest
class ProductControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ProductService productService;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	ImageJpaRepository imageJpaRepository;

	@Test
	@DisplayName("상품등록시 해당 상품의 id를 반환한다.")
	void saveTest() throws Exception {
		//given
		ProductSaveAndUpdateRequest productSaveAndUpdateRequest = new ProductSaveAndUpdateRequest("상품명", 1L, 100000L,
			"상품내용", 1L,
			Arrays.asList(1L, 2L));
		String request = objectMapper.writeValueAsString(productSaveAndUpdateRequest);

		saveDummyImage("imageTest1");
		saveDummyImage("imageTest2");

		//when & then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
			.andExpect(jsonPath("$.id").exists())
			.andExpect(status().isCreated());
	}

	private void saveDummyImage(String image) {
		imageJpaRepository.save(Image.builder().imgUrl(image).build());
	}

	@Test
	@DisplayName("상품 상세 조회시 해당 상품의 정보를 포함하는 응답을 반환한다.")
	void findDetail() throws Exception {
		//given
		ProductSaveAndUpdateRequest productSaveAndUpdateRequest = new ProductSaveAndUpdateRequest("상품명", 1L, 100000L,
			"상품내용", 1L,
			Arrays.asList(1L, 2L));

		saveDummyImage("imageTest1");
		saveDummyImage("imageTest2");

		productService.save(productSaveAndUpdateRequest);

		//when & then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/products/{productId}", 1L)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.productName").value(productSaveAndUpdateRequest.getName()))
			.andExpect(jsonPath("$.content").value(productSaveAndUpdateRequest.getContent()))
			.andExpect(jsonPath("$.price").value(productSaveAndUpdateRequest.getPrice()))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("수정 할 상품의 id와 내용을 받아 상품을 수정한다.")
	void updateTest() throws Exception {
		// Given
		ProductSaveAndUpdateRequest productUpdateRequest = new ProductSaveAndUpdateRequest("수정", 2L, 100000L, "수정내용",
			4L,
			Arrays.asList(1L, 2L));

		saveDummyImage("imageTest1");
		saveDummyImage("imageTest2");
		Long productId = productService.save(productUpdateRequest);
		String requestJson = objectMapper.writeValueAsString(productUpdateRequest);

		// When & Then
		mockMvc.perform(MockMvcRequestBuilders.put("/api/products/{productId}", productId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("등록한 상품을 id를 통해 삭제한다 ")
	void deleteTest() throws Exception {
		//given
		ProductSaveAndUpdateRequest productSaveAndUpdateRequest = new ProductSaveAndUpdateRequest("상품명", 1L, 100000L,
			"상품내용", 1L,
			Arrays.asList(1L, 2L));

		saveDummyImage("imageTest1");
		saveDummyImage("imageTest2");

		Long productId = productService.save(productSaveAndUpdateRequest);
		String requestJson = objectMapper.writeValueAsString(productSaveAndUpdateRequest);

		// When & Then
		mockMvc.perform(MockMvcRequestBuilders.put("/api/products/{productId}", productId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("상품 상태 변경시 해당 상품의 상태가 변경 된다")
	void updateStatus() throws Exception {
		//given
		ProductSaveAndUpdateRequest productSaveAndUpdateRequest = new ProductSaveAndUpdateRequest("상품명", 1L, 100000L,
			"상품내용", 1L,
			Arrays.asList(1L, 2L));

		saveDummyImage("imageTest1");
		saveDummyImage("imageTest2");

		productService.save(productSaveAndUpdateRequest);

		ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest("예약중");
		String request = objectMapper.writeValueAsString(productUpdateRequest);

		//when & then
		mockMvc.perform(MockMvcRequestBuilders.put("/api/products/{productId}/status", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
			.andExpect(status().isOk());
	}
}
