package com.codesquad.secondhand.domain.region.controller;


import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Pageable;


import com.codesquad.secondhand.domain.region.dto.response.RegionSearchAndPageResponse;
import com.codesquad.secondhand.domain.region.service.RegionService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RegionController {

	private final RegionService regionService;

	@GetMapping("/regions")
	public ResponseEntity<List<RegionSearchAndPageResponse>> getAllRegionsAndSearch( @RequestParam(defaultValue = "5") int page, @RequestParam(defaultValue = "0") int offset, @RequestParam(required = false) String word) {
		Pageable pageable = PageRequest.of(offset,page);
		List<RegionSearchAndPageResponse> response = regionService.getAllAndSearch(pageable,word);
		return ResponseEntity.ok(response);
	}
}
