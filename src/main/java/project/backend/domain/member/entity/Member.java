package project.backend.domain.member.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.backend.domain.memberTicketLike.entity.MemberTicketLike;
import project.backend.domain.common.entity.BaseEntity;
import project.backend.domain.onboardingmembercategory.entity.OnboardingMemberCategory;
import project.backend.domain.ticket.entity.Ticket;
import project.backend.domain.member.dto.MemberPatchRequestDto;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // todo : IDENTITY와 AUTO 차이점이 뭔지?
    @Column(name = "memberId")
    public Long id;

    @Enumerated(value = EnumType.STRING)
    public SocialType socialType;

    @Column(name = "socialId")
    public String socialId;

    @Column(name = "nickname")
    public String nickname;

    @Column(name = "profileUrl")
    public String profileUrl;

    @Column(name = "refreshToken")
    public String refreshToken;

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Ticket> tickets = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public List<MemberTicketLike> memberTicketLikes = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public List<OnboardingMemberCategory> onboardingMemberCategories = new ArrayList<>();

    @Builder
    public Member(SocialType socialType, String socialId, String nickname, String profileUrl, String refreshToken){
        this.socialType = socialType;
        this.socialId = socialId;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.refreshToken = refreshToken;
    }

    // Patch
    public Member patchMember(MemberPatchRequestDto memberPatchRequestDto){
        this.nickname = Optional.ofNullable(memberPatchRequestDto.getNickname()).orElse(this.nickname);
        this.profileUrl = Optional.ofNullable(memberPatchRequestDto.getProfileUrl()).orElse(this.profileUrl);
        this.refreshToken = Optional.ofNullable(memberPatchRequestDto.getRefreshToken()).orElse(this.refreshToken);
        return this;
    }
}
