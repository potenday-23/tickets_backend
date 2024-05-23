package project.backend.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.domain.category.service.CategoryService;
import project.backend.domain.member.dto.MemberMyPageResponseDto;
import project.backend.domain.member.dto.MemberPatchRequestDto;
import project.backend.domain.member.dto.MemberStatisticsResponseDto;
import project.backend.domain.member.dto.MemberYearStatisticsResponseDto;
import project.backend.domain.member.entity.Agree;
import project.backend.domain.member.entity.SocialType;
import project.backend.domain.member.entity.Member;
import project.backend.domain.member.mapper.MemberMapper;
import project.backend.domain.member.repository.MemberRepository;
import project.backend.domain.memberTicketLike.repository.MemberTicketLikeRepository;
import project.backend.domain.onboardingmembercategory.service.OnboardingMemberCategoryService;
import project.backend.domain.ticket.repository.TicketRepository;
import project.backend.global.error.exception.BusinessException;
import project.backend.global.error.exception.ErrorCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final CategoryService categoryService;
    private final OnboardingMemberCategoryService onboardingMemberCategoryService;
    private final TicketRepository ticketRepository;
    private final MemberTicketLikeRepository memberTicketLikeRepository;


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
                .socialType(socialType)
                .marketingAgree(Agree.DISAGREE)
                .pushAgree(Agree.DISAGREE).build();
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
//        Boolean isNicknameChange = !(memberPatchRequestDto.getNickname() == null);
//        Boolean isNotOneMonthBefore = member.getNicknameChangeDate() != null ? !member.getNicknameChangeDate().isBefore(LocalDateTime.now().minusMonths(1)) : false;
//
//        if (isNicknameChange) {
//            if (isNotOneMonthBefore) {
//                throw new BusinessException(ErrorCode.MEMBER_NICKNAME_CHANGE_MONTH);
//            }
//            member.nicknameChangeDate = LocalDateTime.now();
//        }
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
