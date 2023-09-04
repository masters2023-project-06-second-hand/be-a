package com.codesquad.secondhand.domain.region.repository;

import static com.codesquad.secondhand.domain.region.entity.QRegion.*;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.codesquad.secondhand.domain.region.entity.Region;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RegionQueryRepository {

	private final JPAQueryFactory query;

	public List<Region> findAll(Pageable pageable, String word) {

		JPAQuery<Region> sql = query.select(region).from(region);
		if(word != null && !word.isEmpty()) {
			sql.where(region.name.containsIgnoreCase(word));
		}
		return sql.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}

}
