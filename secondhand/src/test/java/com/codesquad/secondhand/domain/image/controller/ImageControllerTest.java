package com.codesquad.secondhand.domain.image.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.codesquad.secondhand.annotation.IntegrationTest;
import com.codesquad.secondhand.domain.image.dto.request.ImageDeleteRequest;
import com.codesquad.secondhand.domain.image.service.ImageService;
import com.fasterxml.jackson.databind.ObjectMapper;

@IntegrationTest
class ImageControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ImageService imageService;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	@DisplayName("이미지를 S3에 업로드후 이미지 URL을 DB에 저장한다음 DB에 저장된 이미지의 ID를 응답으로 전송한다.")
	void uploadImage() throws Exception {
		//given
		Path path = Paths.get("src/test/resources/test-image.jpg");
		String parameterName = "file";
		String originalFileName = "test-image.jpg";
		String contentType = "image/jpeg";
		byte[] content = Files.readAllBytes(path);

		MockMultipartFile mockMultipartFile = new MockMultipartFile(parameterName, originalFileName, contentType,
			content);

		//when & then
		mockMvc.perform(MockMvcRequestBuilders.multipart("/api/images")
				.file(mockMultipartFile))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("이미지 id 를 입력받아 해당 이미지를 삭제한다.")
	void deleteImage() throws Exception {
		//given
		Path path = Paths.get("src/test/resources/test-image.jpg");
		String parameterName = "file";
		String originalFileName = "test-image.jpg";
		String contentType = "image/jpeg";
		byte[] content = Files.readAllBytes(path);

		MockMultipartFile mockMultipartFile = new MockMultipartFile(parameterName, originalFileName, contentType,
			content);

		ImageDeleteRequest requestDto = new ImageDeleteRequest(imageService.uploadImg(mockMultipartFile).getId());
		String request = objectMapper.writeValueAsString(requestDto);

		// when & then
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/api/images").contentType(MediaType.APPLICATION_JSON).content(request))
			.andExpect(status().isOk());
	}
}
