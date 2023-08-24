package com.codesquad.secondhand.domain.member.entity;

import com.codesquad.secondhand.domain.member_region.entity.MemberRegion;
import com.codesquad.secondhand.domain.region.entity.Region;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickname;
    private String email;
    private Long selectedRegion;
    private String profileImg;
    @OneToMany(mappedBy = "member")
    private List<MemberRegion> memberRegions = new ArrayList<>();


    @Builder
    public Member(Long id, String nickname, String email, Long selectedRegion, String profileImg) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.selectedRegion = selectedRegion;
        this.profileImg = profileImg;
    }
}
