package com.codesquad.secondhand.domain.region.repository;

import static com.codesquad.secondhand.domain.region.entity.QRegion.*;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.codesquad.secondhand.domain.region.entity.Region;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RegionQueryRepository {

	private final JPAQueryFactory query;

	public Slice<Region> findAll(Pageable pageable, String word) {
		JPAQuery<Region> sql = query.select(region)
			.from(region)
			.where(isRegionNameContains(word))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1);

		List<Region> results = sql.fetch();
		boolean hasNext = results.size() > pageable.getPageSize();

		if (hasNext) {
			results.remove(results.size() - 1);
		}

		return new SliceImpl<>(results, pageable, hasNext);
	}

	private BooleanExpression isRegionNameContains(String word) {
		if (word == null) {
			return null;
		}
		return region.name.contains(word);
	}

}
