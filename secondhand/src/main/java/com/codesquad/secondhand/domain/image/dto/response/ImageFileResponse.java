package com.codesquad.secondhand.domain.image.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ImageFileResponse {
	private Long id;
	private String imgUrl;

	public static ImageFileResponse of(Long id, String imgUrl) {
		return new ImageFileResponse(id, imgUrl);
	}
}
