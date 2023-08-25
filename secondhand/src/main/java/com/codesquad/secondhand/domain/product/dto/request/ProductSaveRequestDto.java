package com.codesquad.secondhand.domain.product.dto.request;

import java.util.List;

import com.codesquad.secondhand.domain.category.entity.Category;
import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.product.entity.Product;
import com.codesquad.secondhand.domain.region.entity.Region;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSaveRequestDto {

	private String name;
	private Long categoryId;
	private Long price;
	private String content;
	private Long regionId;
	private List<Long> imagesId;

	public Product toEntity(Category category, Region region, Member member) {
		return Product.builder()
			.name(this.name)
			.price(this.price)
			.content(this.content)
			.region(region)
			.category(category)
			.member(member)
			.build();
	}
}
