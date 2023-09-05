package com.codesquad.secondhand.domain.image.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.codesquad.secondhand.amazon.S3Uploader;
import com.codesquad.secondhand.domain.image.dto.response.ImageFileResponse;
import com.codesquad.secondhand.domain.product.entity.Image;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ImageService {
	private final ImageQueryService imageQueryService;
	private final S3Uploader s3Uploader;

	@Transactional
	public ImageFileResponse uploadProductImage(MultipartFile multipartFile) {
		String imageUrl = s3Uploader.upload(multipartFile);
		Long imageId = saveImageToDatabase(imageUrl);
		return ImageFileResponse.of(imageId, imageUrl);
	}

	public String uploadMemberImage(MultipartFile multipartFile) {
		String imageUrl = s3Uploader.upload(multipartFile);
		return imageUrl;
	}

	private Long saveImageToDatabase(String imageUrl) {
		Image image = Image.builder().imgUrl(imageUrl).build();
		return imageQueryService.save(image);
	}

	@Transactional
	public void delete(Long imageId) {
		imageQueryService.findById(imageId);
		imageQueryService.deleteById(imageId);
	}

}
