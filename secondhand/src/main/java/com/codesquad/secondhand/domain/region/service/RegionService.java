package com.codesquad.secondhand.domain.region.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.domain.region.entity.Region;
import com.codesquad.secondhand.domain.region.repository.RegionJpaRepository;
import com.codesquad.secondhand.exception.CustomRuntimeException;
import com.codesquad.secondhand.exception.errorcode.RegionException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegionService {
	private final RegionJpaRepository regionJpaRepository;

	public Region findById(Long regionId) {
		return regionJpaRepository.findById(regionId).orElseThrow(() -> new CustomRuntimeException(
			RegionException.REGION_NOT_FOUND));
	}

	public List<Region> findByIds(List<Long> regionIds) {
		return regionIds.stream()
			.map(regionId -> regionJpaRepository.findById(regionId)
				.orElseThrow(() -> new CustomRuntimeException(RegionException.REGION_NOT_FOUND)))
			.collect(Collectors.toUnmodifiableList());
	}
}
