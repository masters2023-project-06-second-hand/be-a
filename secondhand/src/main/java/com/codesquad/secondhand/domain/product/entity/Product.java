package com.codesquad.secondhand.domain.product.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.DynamicInsert;

import com.codesquad.secondhand.domain.category.entity.Category;
import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.region.entity.Region;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DynamicInsert
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private Long price;
	private String content;
	private LocalDateTime createdAt;
	private Long status;
	private Long viewCount;
	@ManyToOne
	@JoinColumn(name = "region_id")
	private Region region;
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;
	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	@Builder
	public Product(Long id, Region region, Category category, Member member, String name, Long price,
		String content, Long status, Long viewCount) {
		this.id = id;
		this.region = region;
		this.category = category;
		this.member = member;
		this.name = name;
		this.price = price;
		this.content = content;
		this.status = status;
		this.viewCount = viewCount;
	}
}
