package project.backend.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.domain.category.service.CategoryService;
import project.backend.domain.member.dto.*;
import project.backend.domain.member.entity.SocialType;
import project.backend.domain.member.entity.Member;
import project.backend.domain.member.mapper.MemberMapper;
import project.backend.domain.member.repository.MemberRepository;
import project.backend.domain.onboardingmembercategory.service.OnboardingMemberCategoryService;
import project.backend.domain.ticket.repository.TicketRepository;
import project.backend.global.error.exception.BusinessException;
import project.backend.global.error.exception.ErrorCode;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final CategoryService categoryService;
    private final OnboardingMemberCategoryService onboardingMemberCategoryService;
    private final TicketRepository ticketRepository;
    private final MemberJwtService memberJwtService;


    /**
     * socialId와 socialType 기준 Member 반환
     *
     * @param socialId
     * @param socialType
     * @return Member
     */
    public Member getMemberBySocial(String socialId, SocialType socialType, String email) {

        Member member = memberRepository.findFirstBySocialIdAndSocialType(socialId, socialType)
                .orElseGet(() -> createMember(socialId, socialType));
        if (email != null && member.email == null) {
            member.email = email;
            memberRepository.save(member);
        }
        return member;
    }

    /**
     * socialId와 socialType를 가지고 있는 Member 생성
     *
     * @param socialId
     * @param socialType
     * @return Memeber
     */
    public Member createMember(String socialId, SocialType socialType) {
        Member member = Member.builder()
                .socialId(socialId)
                .socialType(socialType).build();
        memberRepository.save(member);
        return member;
    }

    /**
     * 닉네임 중복 검사
     *
     * @param nickname
     * @return
     */
    @Transactional(readOnly = true)
    public void verifiedNickname(String nickname) {
        if (nickname != null && memberRepository.findAllByNickname(nickname).size() > 0) {
            throw new BusinessException(ErrorCode.NICKNAME_DUPLICATE);
        }
    }

    /**
     * 회원가입
     *
     * @param memberSignupDto
     * @return Member
     */
    public Member setMemberSignup(MemberSignupDto memberSignupDto) {
        Member member = memberJwtService.getMember();

        member.signupMember(memberSignupDto);
        memberRepository.save(member);

        return member;
    }

    /**
     * FCM 토큰 등록
     * @param fcmToken
     * @return Member
     */
    public void setFcmToken(String fcmToken) {
        Member member = memberJwtService.getMember();
        member.fcmToken = fcmToken;
        memberRepository.save(member);
    }


    @Transactional(readOnly = true)
    public Member getMemberBySocialIdAndSocialType(String socialId, SocialType socialType) {
        return memberRepository.findFirstBySocialIdAndSocialType(socialId, socialType).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Member getMember(Long id) {
        return verifiedMember(id);
    }

    @Transactional(readOnly = true)
    public List<Member> getMemberList() {
        return memberRepository.findAll();
    }

    public Member patchMember(Long id, MemberPatchRequestDto memberPatchRequestDto) {
        Member member = verifiedMember(id);
        member.patchMember(memberPatchRequestDto);
        memberRepository.save(member);
        return member;
    }

    public Member onboardingMember(Long id, List<String> categorys) {
        Member member = verifiedMember(id);
        onboardingMemberCategoryService.deleteOnboardingMemberCategoryByMember(member);
        for (String category : categorys) {
            onboardingMemberCategoryService.createOnboardingMemberCategory(member, categoryService.verifiedCategory(category));
        }
        return member;
    }

    public List<MemberStatisticsResponseDto> getMemberStatistics(Member member, String month) {
        return ticketRepository.getStatisticsList(member, month);
    }

    public List<MemberYearStatisticsResponseDto> getMemberYearStatistics(Member member) {
        return ticketRepository.getYearStatisticsList(member);
    }

    public MemberMyPageResponseDto getMyPage(Member member) {
        // 통계
        MemberStatisticsResponseDto memberStatisticsResponseDto = ticketRepository.getStatisticsList(member, null).get(0);
        String statistics = memberStatisticsResponseDto.getCategory() + " " + memberStatisticsResponseDto.getCategoryPercent() + "%";

        // 응답
        MemberMyPageResponseDto memberMyPageResponseDto = memberMapper.MemberToMemberMyPageResponseDto(member);
        memberMyPageResponseDto.setMyTicketCount(member.getTickets().size());
        memberMyPageResponseDto.setMyStatistics(statistics);
        memberMyPageResponseDto.setMyLikeCount(member.getMemberTicketLikes().size());

        return memberMyPageResponseDto;
    }

    public void deleteMember(Long id) {
        memberRepository.delete(verifiedMember(id));
    }

    public Member verifiedMember(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }

}
