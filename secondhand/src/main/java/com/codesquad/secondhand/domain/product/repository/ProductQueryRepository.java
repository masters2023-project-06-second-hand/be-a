package com.codesquad.secondhand.domain.product.repository;

import static com.codesquad.secondhand.domain.product.entity.QProduct.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.codesquad.secondhand.domain.member.entity.Member;
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
				isProductRegionIdEquals(regionId),
				isProductCategoryIdEquals(categoryId)
			)
			.fetch();
	}

	private BooleanExpression isProductRegionIdEquals(Long regionId) {
		return product.region.id.eq(regionId);
	}

	private BooleanExpression isProductCategoryIdEquals(Long categoryId) {
		if (categoryId == null) {
			return null;
		}
		return product.category.id.eq(categoryId);
	}

	public List<Product> findSalesProduct(Member member, Integer statusId) {
		return query.select(product)
			.from(product)
			.where(
				isSameMember(member),
				isSameStatusId(statusId)
			)
			.fetch();
	}

	private BooleanExpression isSameMember(Member member) {
		return product.member.eq(member);
	}

	/**
	 * statusId = 0 : 판매중
	 * statusId = 1 : 예약중
	 * statusId = 2 : 판매완료
	 * @param statusId
	 * @return
	 */
	private BooleanExpression isSameStatusId(Integer statusId) {
		if (statusId == null) {
			return null;
		} else if (statusId.equals(0) || statusId.equals(1)) {
			return product.status.eq(0).or(product.status.eq(1));
		}
		return product.status.eq(statusId);
	}
}

