package com.codesquad.secondhand.domain.product.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import com.codesquad.secondhand.domain.category.entity.Category;
import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.product.dto.request.ProductSaveAndUpdateRequest;
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
	@Column(updatable = false)
	@CreationTimestamp
	private LocalDateTime createdAt;
	private int status;
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
	@OneToMany(mappedBy = "product")
	private List<Image> images = new ArrayList<>();

	@Builder
	public Product(Long id, Region region, Category category, Member member, String name, Long price,
		String content, int status, Long viewCount) {
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

	public void updateFromDto(ProductSaveAndUpdateRequest requestDto, Category category, Region region) {
		this.name = requestDto.getName();
		this.price = requestDto.getPrice();
		this.content = requestDto.getContent();
		this.category = category;
		this.region = region;
	}

	public void changeStatus(int status) {
		this.status = status;
	}
}
