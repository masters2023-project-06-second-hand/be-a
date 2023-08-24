package com.codesquad.secondhand.domain.member_region.entity;

import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.region.entity.Region;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
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
}
