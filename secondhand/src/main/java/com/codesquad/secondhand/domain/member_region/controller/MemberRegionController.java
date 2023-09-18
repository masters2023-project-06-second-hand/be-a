package com.codesquad.secondhand.domain.member_region.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codesquad.secondhand.domain.member.dto.request.RegionRequest;
import com.codesquad.secondhand.domain.member.dto.response.MemberRegionResponse;
import com.codesquad.secondhand.domain.member_region.service.MemberRegionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberRegionController {

	private final MemberRegionService memberRegionService;

	@PostMapping("/members/{memberId}/regions")
	public ResponseEntity addRegion(@PathVariable Long memberId, @RequestBody RegionRequest regionRequest) {
		memberRegionService.addRegion(memberId, regionRequest);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@DeleteMapping("/members/{memberId}/regions")
	public ResponseEntity deleteRegion(@PathVariable Long memberId, @RequestBody RegionRequest regionRequest) {
		memberRegionService.deleteRegion(memberId, regionRequest);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/members/{memberId}/regions")
	public ResponseEntity updateSelectedRegion(@PathVariable Long memberId, @RequestBody RegionRequest regionRequest) {
		memberRegionService.updateSelectedRegion(memberId, regionRequest);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/members/{memberId}/regions")
	public ResponseEntity<MemberRegionResponse> getRegions(@PathVariable Long memberId) {
		MemberRegionResponse memberRegionResponse = memberRegionService.getRegion(memberId);
		return ResponseEntity.ok(memberRegionResponse);
	}
}
