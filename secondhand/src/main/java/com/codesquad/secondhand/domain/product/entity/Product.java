package com.codesquad.secondhand.domain.product.entity;

import com.codesquad.secondhand.domain.category.entity.Category;
import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.region.entity.Region;
import com.sun.istack.NotNull;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DynamicInsert
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    private String name;
    private Long price;
    private String description;
    private LocalDateTime createdAt;
    private Long status;
    private Long viewCount;

    @Builder
    public Product(Long id, Region region, Category category, Member member, String name, Long price,
                   String description,
                   LocalDateTime createdAt, Long status, Long viewCount) {
        this.id = id;
        this.region = region;
        this.category = category;
        this.member = member;
        this.name = name;
        this.price = price;
        this.description = description;
        this.createdAt = createdAt;
        this.status = status;
        this.viewCount = viewCount;
    }
}
