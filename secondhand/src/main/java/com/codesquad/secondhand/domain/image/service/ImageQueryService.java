package com.codesquad.secondhand.domain.image.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.domain.product.entity.Image;
import com.codesquad.secondhand.domain.product.repository.ImageJpaRepository;
import com.codesquad.secondhand.exception.CustomRuntimeException;
import com.codesquad.secondhand.exception.errorcode.ImageException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageQueryService {

	private final ImageJpaRepository imageJpaRepository;

	public Image findById(Long id) {
		return imageJpaRepository.findById(id).orElseThrow(() -> new CustomRuntimeException(
			ImageException.IMAGE_NOT_FOUND));
	}

	@Transactional
	public void deleteById(Long id) {
		imageJpaRepository.deleteById(id);
	}

	@Transactional
	public Long save(Image image) {
		return imageJpaRepository.save(image).getId();
	}

}
