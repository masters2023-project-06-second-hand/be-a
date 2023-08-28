package com.codesquad.secondhand.domain.image.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.codesquad.secondhand.domain.image.dto.ImageFileResponseDto;
import com.codesquad.secondhand.exception.CustomRuntimeException;
import com.codesquad.secondhand.exception.errorcode.FileUploadException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImageService {
	private final AmazonS3 amazonS3;
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	public ImageFileResponseDto uploadImg(MultipartFile multipartFile) {
		validateFileExists(multipartFile);
		String uuid = UUID.randomUUID().toString(); // 고유한 파일 이름 생성

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(multipartFile.getSize());
		metadata.setContentType(multipartFile.getContentType());

		uploadToS3(multipartFile, uuid, metadata);
		
		String url = amazonS3.getUrl(bucket, uuid).toString();

		return ImageFileResponseDto.of(1L, url);
	}

	private void uploadToS3(MultipartFile multipartFile, String uuid, ObjectMetadata metadata) {
		try {
			amazonS3.putObject(bucket, uuid, multipartFile.getInputStream(), metadata);
		} catch (IOException e) {
			throw new CustomRuntimeException(FileUploadException.FILE_READING);
		}
	}

	private void validateFileExists(MultipartFile multipartFile) {
		if (multipartFile.isEmpty()) {
			throw new CustomRuntimeException(FileUploadException.EMPTY_FILE);
		}
	}

}
