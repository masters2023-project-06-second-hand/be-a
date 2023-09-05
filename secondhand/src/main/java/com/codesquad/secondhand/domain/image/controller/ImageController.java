package com.codesquad.secondhand.domain.image.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codesquad.secondhand.domain.image.dto.response.ImageFileResponse;
import com.codesquad.secondhand.domain.image.service.ImageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ImageController {

	private final ImageService imageService;

	@PostMapping("/products/images")
	public ResponseEntity<ImageFileResponse> uploadProductImage(
		@RequestPart(value = "file") MultipartFile multipartFile) {
		ImageFileResponse response = imageService.uploadProductImage(multipartFile);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping("/members/images")
	public ResponseEntity<Map<String, String>> uploadMemberImage(
		@RequestPart(value = "file") MultipartFile multipartFile) {
		String imageUrl = imageService.uploadMemberImage(multipartFile);
		return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("imgUrl", imageUrl));
	}

	@DeleteMapping("/images/{imageId}")
	public ResponseEntity delete(@PathVariable Long imageId) {
		imageService.delete(imageId);
		return ResponseEntity.noContent().build();
	}
}
