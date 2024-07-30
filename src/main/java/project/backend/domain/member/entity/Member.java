package project.backend.domain.member.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.backend.domain.keyword.entity.CulturalEventSearchKeyword;
import project.backend.domain.like.entity.CulturalEventLike;
import project.backend.domain.member.MemberListener;
import project.backend.domain.member.dto.MemberInfoDto;
import project.backend.domain.common.entity.BaseEntity;
import project.backend.domain.onboardingmembercategory.entity.OnboardingMemberCategory;
import project.backend.domain.ticket.entity.Ticket;
import project.backend.domain.member.dto.MemberPatchRequestDto;
import project.backend.domain.ticketfolder.entity.TicketFolder;
import project.backend.domain.visit.entity.CulturalEventVisit;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@EntityListeners(MemberListener.class)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // todo : IDENTITY와 AUTO 차이점이 뭔지?
    public Long id;

    @Enumerated(value = EnumType.STRING)
    public SocialType socialType;

    @Enumerated(value = EnumType.STRING)
    public Gender gender;

    public String socialId;

    public String nickname;

    public String email;

    public LocalDate birthday;

    public LocalDateTime nicknameChangeDate;

    public String profileImageUrl;

    public String refreshToken;

    public String fcmToken;

    public Boolean isSignup = false;

    public Boolean isMarketingAgree = false;

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public List<Ticket> tickets = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public List<OnboardingMemberCategory> onboardingMemberCategories = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public List<CulturalEventLike> culturalEventLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public List<CulturalEventVisit> culturalEventVisitList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public List<CulturalEventSearchKeyword> culturalEventSearchKeywordList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public List<TicketFolder> ticketFolderList = new ArrayList<>();
    @Builder
    public Member(SocialType socialType, String socialId, String nickname, String profileImageUrl, String refreshToken) {
        this.socialType = socialType;
        this.socialId = socialId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.refreshToken = refreshToken;
    }

    // Patch
    public Member patchMember(MemberPatchRequestDto memberPatchRequestDto) {
        this.nickname = Optional.ofNullable(memberPatchRequestDto.getNickname()).orElse(this.nickname);
        this.profileImageUrl = Optional.ofNullable(memberPatchRequestDto.getProfileImageUrl()).orElse(this.profileImageUrl);
        this.refreshToken = Optional.ofNullable(memberPatchRequestDto.getRefreshToken()).orElse(this.refreshToken);
        return this;
    }

    public Member signupMember(MemberInfoDto memberInfoDto) {
        this.nickname = memberInfoDto.nickname != null ? memberInfoDto.nickname : this.nickname;
        this.email = memberInfoDto.email != null ? memberInfoDto.email : this.email;
        this.birthday = memberInfoDto.birthday != null ? memberInfoDto.birthday : this.birthday;
        this.gender = memberInfoDto.gender != null ? memberInfoDto.gender : this.gender;
        this.isMarketingAgree = memberInfoDto.isMarketingAgree != null ? memberInfoDto.isMarketingAgree : this.isMarketingAgree;
        this.profileImageUrl = memberInfoDto.profileImageUrl != null ? memberInfoDto.profileImageUrl : this.profileImageUrl;

        this.isSignup = this.nickname != null && this.email != null && this.birthday != null && this.gender != null;

        return this;
    }
}
