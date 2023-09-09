package com.codesquad.secondhand.domain.image.service;

import static org.mockito.BDDMockito.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;

import com.codesquad.secondhand.amazon.S3Uploader;
import com.codesquad.secondhand.annotation.ServiceIntegrationTest;
import com.codesquad.secondhand.domain.image.dto.response.ImageFileResponse;
import com.codesquad.secondhand.domain.product.entity.Image;

@ServiceIntegrationTest
class ImageServiceTest {

	@Autowired
	ImageService imageService;

	@Autowired
	ImageQueryService imageQueryService;

	@MockBean
	S3Uploader s3Uploader;

	@Test
	@DisplayName("이미지 업로드시 해당 이미지를 S3에 업로드후 이미지 주소를 DB에 저장한다.")
	void uploadProductImage() throws IOException {
		//given
		Path path = Paths.get("src/test/resources/test-image.jpg");
		String parameterName = "file";
		String originalFileName = "test-image.jpg";
		String contentType = "image/jpeg";
		byte[] content = Files.readAllBytes(path);

		MockMultipartFile mockMultipartFile = new MockMultipartFile(parameterName, originalFileName, contentType,
			content);

		given(s3Uploader.upload(any(MockMultipartFile.class))).willReturn("imageUrl");

		//when
		ImageFileResponse expected = imageService.uploadProductImage(mockMultipartFile);

		//then
		Image image = imageQueryService.findById(expected.getId());
		Assertions.assertThat(image.getImgUrl()).isEqualTo(expected.getImgUrl());
	}

}
