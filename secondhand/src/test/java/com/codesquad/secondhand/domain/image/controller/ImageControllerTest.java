package com.codesquad.secondhand.domain.image.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.codesquad.secondhand.BaseControllerTest;
import com.codesquad.secondhand.annotation.ControllerIntegrationTest;

@ControllerIntegrationTest
class ImageControllerTest extends BaseControllerTest {

	@BeforeEach
	void setUp() {
		given(s3Uploader.upload(any(MockMultipartFile.class))).willReturn("imageUrl");
	}

	@Test
	@DisplayName("상품 이미지를 S3에 업로드후 이미지 URL을 DB에 저장한다음 DB에 저장된 이미지의 ID를 응답으로 전송한다.")
	void uploadProductImage() throws Exception {
		//given
		Path path = Paths.get("src/test/resources/test-image.jpg");
		String parameterName = "file";
		String originalFileName = "test-image.jpg";
		String contentType = "image/jpeg";
		byte[] content = Files.readAllBytes(path);

		MockMultipartFile mockMultipartFile = new MockMultipartFile(parameterName, originalFileName, contentType,
			content);

		//when & then
		mockMvc.perform(MockMvcRequestBuilders.multipart("/api/products/images")
				.file(mockMultipartFile)
				.header(AUTHORIZATION, JWT_TOKEN_PREFIX + jwt.getAccessToken()))
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("회원 이미지를 S3에 업로드후 이미지의 URL을 포함한 응답을 전송한다.")
	void uploadMemberImage() throws Exception {
		//given
		Path path = Paths.get("src/test/resources/test-image.jpg");
		String parameterName = "file";
		String originalFileName = "test-image.jpg";
		String contentType = "image/jpeg";
		byte[] content = Files.readAllBytes(path);

		MockMultipartFile mockMultipartFile = new MockMultipartFile(parameterName, originalFileName, contentType,
			content);

		//when & then
		mockMvc.perform(MockMvcRequestBuilders.multipart("/api/members/images")
				.file(mockMultipartFile)
				.header(AUTHORIZATION, JWT_TOKEN_PREFIX + jwt.getAccessToken()))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.imgUrl").exists());
	}

	@Test
	@DisplayName("상품 이미지 id 를 입력받아 해당 이미지를 삭제한다.")
	void deleteProductImage() throws Exception {
		//given
		Path path = Paths.get("src/test/resources/test-image.jpg");
		String parameterName = "file";
		String originalFileName = "test-image.jpg";
		String contentType = "image/jpeg";
		byte[] content = Files.readAllBytes(path);

		MockMultipartFile mockMultipartFile = new MockMultipartFile(parameterName, originalFileName, contentType,
			content);
		imageService.uploadProductImage(mockMultipartFile);

		// when & then
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/api/images/{imageId}", 1L)
					.header(AUTHORIZATION, JWT_TOKEN_PREFIX + jwt.getAccessToken())
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}
}
