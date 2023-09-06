package com.codesquad.secondhand.amazon;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.codesquad.secondhand.exception.CustomRuntimeException;
import com.codesquad.secondhand.exception.errorcode.FileUploadException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class S3Uploader {
	private final AmazonS3 amazonS3;
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	public String upload(MultipartFile multipartFile) {
		validateFileExists(multipartFile);

		String uniqueFileName = generateUniqueFileName();
		ObjectMetadata metadata = createMetadataFrom(multipartFile);

		String imageUrl = uploadToS3AndGetUrl(multipartFile, uniqueFileName, metadata);

		return imageUrl;
	}

	private void validateFileExists(MultipartFile multipartFile) {
		if (multipartFile.isEmpty()) {
			throw new CustomRuntimeException(FileUploadException.EMPTY_FILE);
		}
	}

	private String generateUniqueFileName() {
		return UUID.randomUUID().toString();
	}

	private ObjectMetadata createMetadataFrom(MultipartFile multipartFile) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(multipartFile.getSize());
		metadata.setContentType(multipartFile.getContentType());
		return metadata;
	}

	private String uploadToS3AndGetUrl(MultipartFile multipartFile, String fileName, ObjectMetadata metadata) {
		uploadToS3(multipartFile, fileName, metadata);
		return amazonS3.getUrl(bucket, fileName).toString();
	}

	private void uploadToS3(MultipartFile multipartFile, String uuid, ObjectMetadata metadata) {
		try {
			amazonS3.putObject(bucket, uuid, multipartFile.getInputStream(), metadata);
		} catch (IOException e) {
			throw new CustomRuntimeException(FileUploadException.FILE_READING);
		}
	}
}
