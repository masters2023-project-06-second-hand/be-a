package com.codesquad.secondhand.domain.product.repository;

import static com.codesquad.secondhand.domain.product.entity.QProduct.*;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.product.entity.Product;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductQueryRepository {

	private final JPAQueryFactory query;

	public Slice<Product> findAll(Long regionId, Long categoryId, Pageable pageable) {
		JPAQuery<Product> sql = query.select(product)
			.from(product)
			.where(
				isProductRegionIdEquals(regionId),
				isProductCategoryIdEquals(categoryId)
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1);

		List<Product> results = sql.fetch();
		boolean hasNext = results.size() > pageable.getPageSize();

		if (hasNext) {
			results.remove(results.size() - 1);
		}

		return new SliceImpl<>(results, pageable, hasNext);
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
	 *
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

