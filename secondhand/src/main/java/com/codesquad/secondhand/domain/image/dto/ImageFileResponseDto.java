package com.codesquad.secondhand.domain.image.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ImageFileResponseDto {
	private Long id;
	private String imgUrl;

	public static ImageFileResponseDto of(Long id, String imgUrl) {
		return new ImageFileResponseDto(id, imgUrl);
	}
}
