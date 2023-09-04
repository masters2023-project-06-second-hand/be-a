package com.codesquad.secondhand.domain.product.repository;

import static com.codesquad.secondhand.domain.product.entity.QProduct.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.codesquad.secondhand.domain.product.entity.Product;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductQueryRepository {

	private final JPAQueryFactory query;

	public List<Product> findAll(Long regionId, Long categoryId) {
		return query.select(product)
			.from(product)
			.where(
				regionId(regionId),
				categoryId(categoryId)
			)
			.fetch();
	}

	private BooleanExpression regionId(Long regionId) {
		return product.region.id.eq(regionId);
	}

	private BooleanExpression categoryId(Long categoryId) {
		if (categoryId == null) {
			return null;
		}
		return product.category.id.eq(categoryId);
	}
}

