package com.codesquad.secondhand.domain.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codesquad.secondhand.domain.product.entity.Image;

public interface ImageJpaRepository extends JpaRepository<Image, Long> {
}
