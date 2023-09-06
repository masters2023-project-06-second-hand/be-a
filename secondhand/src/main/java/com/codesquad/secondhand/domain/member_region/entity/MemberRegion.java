package com.codesquad.secondhand.domain.member_region.entity;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.region.entity.Region;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRegion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne
	@JoinColumn(name = "region_id")
	private Region region;

	@Builder
	public MemberRegion(Long id, Member member, Region region) {
		this.id = id;
		this.member = member;
		this.region = region;
	}

	public static List<MemberRegion> of(Member member, List<Region> regions) {
		return regions.stream()
			.map(region -> MemberRegion.builder()
				.member(member)
				.region(region)
				.build())
			.collect(Collectors.toUnmodifiableList());
	}

	public static MemberRegion of(Member member, Region region) {
		return MemberRegion.builder()
			.member(member)
			.region(region)
			.build();
	}

}
