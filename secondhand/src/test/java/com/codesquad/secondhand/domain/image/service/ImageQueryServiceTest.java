package com.codesquad.secondhand.domain.image.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.codesquad.secondhand.annotation.ServiceIntegrationTest;
import com.codesquad.secondhand.domain.product.entity.Image;
import com.codesquad.secondhand.exception.CustomRuntimeException;

@ServiceIntegrationTest
class ImageQueryServiceTest {

	@Autowired
	ImageQueryService imageQueryService;

	@Test
	@DisplayName("save 메서드를 통해 이미지를 DB에 저장할수 있다.")
	void save() {
		//given
		String imageUrl = "testImage";
		Image excepetedImage = Image.builder().imgUrl(imageUrl).build();

		//when
		Long imageId = imageQueryService.save(excepetedImage);

		//then
		Assertions.assertThat(imageId).isEqualTo(1L);
	}

	@Test
	@DisplayName("ImageId를 통해 특정 이미지를 찾을수 있다.")
	void findById() {
		//given
		String imageUrl = "testImage";
		Image excepetedImage = Image.builder().imgUrl(imageUrl).build();
		Long imageId = imageQueryService.save(excepetedImage);

		//when
		Image actualImage = imageQueryService.findById(imageId);

		//then
		Assertions.assertThat(actualImage.getId()).isEqualTo(imageId);
	}

	@Test
	@DisplayName("ImageId를 통해 특정 이미지를 찾을수 없다면 예외가 발생한다.")
	void findByIdThrowsExceptionWhenImageNotFound() {
		//given
		Long imageId = 100L;

		//when & then
		Assertions.assertThatThrownBy(() -> imageQueryService.findById(imageId))
			.isInstanceOf(CustomRuntimeException.class);
	}

	@Test
	@DisplayName("ImageId를 통해 특정 이미지를 삭제할수 있다.")
	void delete() {
		//given
		String imageUrl = "testImage";
		Image excepetedImage = Image.builder().imgUrl(imageUrl).build();
		Long imageId = imageQueryService.save(excepetedImage);

		//when & then
		imageQueryService.deleteById(imageId);

		Assertions.assertThatThrownBy(() -> imageQueryService.findById(imageId))
			.isInstanceOf(CustomRuntimeException.class);
	}

}
