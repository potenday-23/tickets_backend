package project.backend.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.domain.member.dto.*;
import project.backend.domain.member.entity.SocialType;
import project.backend.domain.member.entity.Member;
import project.backend.domain.member.repository.MemberRepository;
import project.backend.global.error.exception.BusinessException;
import project.backend.global.error.exception.ErrorCode;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberJwtService memberJwtService;


    /**
     * socialId와 socialType 기준 Member 반환
     *
     * @param socialId
     * @param socialType
     * @return Member
     */
    public Member getMemberBySocial(String socialId, String socialType, String email) {
        Member member = memberRepository.findFirstBySocialIdAndSocialType(socialId, SocialType.valueOf(socialType))
                .orElseGet(() -> createMember(socialId, SocialType.valueOf(socialType)));
        if (email != null) {
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
    public void validateNickname(String nickname) {
        Member member = memberJwtService.getMember();
        if (memberRepository.findByNicknameAndIdNot(nickname, member.id).isPresent()) {
            throw new BusinessException(ErrorCode.NICKNAME_DUPLICATE);
        }
    }

    /**
     * 회원가입
     *
     * @param memberInfoDto
     * @return Member
     */
    public Member setMemberInfo(MemberInfoDto memberInfoDto) {
        // 닉네임 유효성 검사
        validateNickname(memberInfoDto.nickname);

        // 추가 정보 입력
        Member member = memberJwtService.getMember();
        member.signupMember(memberInfoDto);
        memberRepository.save(member);

        return member;
    }

    /**
     * FCM 토큰 등록
     *
     * @param fcmToken
     * @return Member
     */
    public void setFcmToken(String fcmToken) {
        Member member = memberJwtService.getMember();
        member.fcmToken = fcmToken;
        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public Member getMember(Long id) {
        return verifiedMember(id);
    }

    public Member patchMember(Long id, MemberPatchRequestDto memberPatchRequestDto) {
        Member member = verifiedMember(id);
        member.patchMember(memberPatchRequestDto);
        memberRepository.save(member);
        return member;
    }

    public void deleteMember(Long id) {
        memberRepository.delete(verifiedMember(id));
    }

    public Member verifiedMember(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }

}
