package com.codesquad.secondhand.domain.product.repository;

import com.codesquad.secondhand.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<Product,Long> {
}
