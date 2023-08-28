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
import com.codesquad.secondhand.domain.product.dto.request.ProductSaveRequestDto;
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
		ProductSaveRequestDto productSaveRequest = new ProductSaveRequestDto("상품명", 1L, 100000L, "상품내용", 1L,
			Arrays.asList(1L, 2L));
		String request = objectMapper.writeValueAsString(productSaveRequest);

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
		ProductSaveRequestDto productSaveRequest = new ProductSaveRequestDto("상품명", 1L, 100000L, "상품내용", 1L,
			Arrays.asList(1L, 2L));

		saveDummyImage("imageTest1");
		saveDummyImage("imageTest2");

		productService.save(productSaveRequest);

		//when & then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/products/{productId}", 1L)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.productName").value(productSaveRequest.getName()))
			.andExpect(jsonPath("$.content").value(productSaveRequest.getContent()))
			.andExpect(jsonPath("$.price").value(productSaveRequest.getPrice()))
			.andExpect(status().isOk());
	}
}
