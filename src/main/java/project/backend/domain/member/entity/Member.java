package project.backend.domain.member.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.backend.domain.like.entity.CulturalEventLike;
import project.backend.domain.memberTicketLike.entity.MemberTicketLike;
import project.backend.domain.common.entity.BaseEntity;
import project.backend.domain.onboardingmembercategory.entity.OnboardingMemberCategory;
import project.backend.domain.ticket.entity.Ticket;
import project.backend.domain.member.dto.MemberPatchRequestDto;
import project.backend.domain.traffic.entity.Traffic;
import project.backend.domain.visit.entity.CulturalEventVisit;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // todo : IDENTITY와 AUTO 차이점이 뭔지?
    public Long id;

    @Enumerated(value = EnumType.STRING)
    public SocialType socialType;

    public String socialId;

    public String nickname;

    public LocalDateTime nicknameChangeDate;

    public String profileUrl;

    public String refreshToken;

    @Enumerated(EnumType.STRING)
    public Agree marketingAgree = Agree.DISAGREE;

    @Enumerated(EnumType.STRING)
    public Agree pushAgree = Agree.DISAGREE;

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Ticket> tickets = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public List<MemberTicketLike> memberTicketLikes = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public List<OnboardingMemberCategory> onboardingMemberCategories = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public List<CulturalEventLike> culturalEventLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public List<CulturalEventVisit> culturalEventVisitList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Traffic> traffics = new ArrayList<>();

    @Builder
    public Member(SocialType socialType, String socialId, String nickname, String profileUrl, String refreshToken, Agree marketingAgree, Agree pushAgree){
        this.socialType = socialType;
        this.socialId = socialId;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.refreshToken = refreshToken;
        this.marketingAgree = marketingAgree;
        this.pushAgree = pushAgree;
    }

    // Patch
    public Member patchMember(MemberPatchRequestDto memberPatchRequestDto){
        this.nickname = Optional.ofNullable(memberPatchRequestDto.getNickname()).orElse(this.nickname);
        this.profileUrl = Optional.ofNullable(memberPatchRequestDto.getProfileUrl()).orElse(this.profileUrl);
        this.refreshToken = Optional.ofNullable(memberPatchRequestDto.getRefreshToken()).orElse(this.refreshToken);
        this.marketingAgree = Optional.ofNullable(memberPatchRequestDto.getMarketingAgree()).orElse(this.marketingAgree);
        this.pushAgree = Optional.ofNullable(memberPatchRequestDto.getPushAgree()).orElse(this.pushAgree);
        return this;
    }
}
