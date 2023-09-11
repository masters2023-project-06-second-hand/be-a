package com.codesquad.secondhand.domain.product.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codesquad.secondhand.domain.product.entity.Product;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {
	@Query("SELECT p FROM Product p JOIN FETCH p.region JOIN FETCH p.category JOIN FETCH p.member JOIN FETCH p.images WHERE p.id = :productId")
	Optional<Product> findById(@Param("productId") Long productId);
}
