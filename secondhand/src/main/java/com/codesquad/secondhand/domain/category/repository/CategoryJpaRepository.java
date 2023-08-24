package com.codesquad.secondhand.domain.category.repository;

import com.codesquad.secondhand.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<Category,Long> {
}
