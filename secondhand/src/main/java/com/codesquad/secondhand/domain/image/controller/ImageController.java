package com.codesquad.secondhand.domain.image.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codesquad.secondhand.domain.image.dto.request.ImageDeleteRequestDto;
import com.codesquad.secondhand.domain.image.dto.response.ImageFileResponseDto;
import com.codesquad.secondhand.domain.image.service.ImageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ImageController {

	private final ImageService imageService;

	@PostMapping("/images")
	public ResponseEntity<ImageFileResponseDto> uploadImg(@RequestPart(value = "file") MultipartFile multipartFile) {
		ImageFileResponseDto response = imageService.uploadImg(multipartFile);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/images")
	public ResponseEntity delete(@RequestBody ImageDeleteRequestDto requestDto) {
		imageService.delete(requestDto.getId());
		return ResponseEntity.ok().build();
	}
}
