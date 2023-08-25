package com.codesquad.secondhand.domain.product.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.codesquad.secondhand.domain.product.dto.request.ProductSaveRequestDto;
import com.codesquad.secondhand.domain.product.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ProductService productService;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	@DisplayName("상품등록시 해당 상품의 id를 반환한다.")
	void saveTest() throws Exception {
		//given
		ProductSaveRequestDto productSaveRequest = new ProductSaveRequestDto("상품명", 1L, 100000L, "상품내용", 1L,
			Arrays.asList(1L, 2L));
		String request = objectMapper.writeValueAsString(productSaveRequest);

		//when & then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
			.andExpect(jsonPath("$.id").exists())
			.andExpect(status().isCreated());
	}

}
