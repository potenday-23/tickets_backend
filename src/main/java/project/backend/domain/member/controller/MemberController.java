package project.backend.domain.member.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.backend.domain.jwt.service.JwtService;
import project.backend.domain.member.dto.*;
import project.backend.domain.member.entity.Member;
import project.backend.domain.member.mapper.MemberMapper;
import project.backend.domain.member.service.LogoutTokenService;
import project.backend.domain.member.service.MemberJwtService;
import project.backend.domain.member.service.MemberService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Api(tags = "B. 멤버")
public class MemberController {

    private final MemberService memberService;
    private final JwtService jwtService;
    private final MemberJwtService memberJwtService;
    private final LogoutTokenService logoutTokenService;
    private final MemberMapper memberMapper;

    @ApiOperation(value = "로그인")
    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody MemberLoginDto request) {

        // Member 확인
        Member member = memberService.getMemberBySocial(request.socialId, request.socialType, request.email);

        // accessToken과 refreshToken 발급
        String accessToken = jwtService.getAccessToken(member);

        // 응답
        MemberRetrieveDto memberRetrieveDto = memberMapper.memberToMemberRetrieveDto(member);
        memberRetrieveDto.setAccessToken(accessToken);
        return new ResponseEntity<>(memberRetrieveDto, HttpStatus.OK);
    }

    @ApiOperation(value = "유저 정보 작성",
            notes = "`isSignup` : 필수값을 다 적었을 경우에만 True로 변경됩니다.")
    @PostMapping("/info")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity info(@Valid @RequestBody MemberInfoDto request) {
        Member member = memberService.setMemberInfo(request);

        // 응답
        MemberRetrieveDto memberRetrieveDto = memberMapper.memberToMemberRetrieveDto(member);
        return new ResponseEntity<>(memberRetrieveDto, HttpStatus.OK);
    }

    @ApiOperation(value = "닉네임 유효성 검사")
    @PostMapping("/nickname-validation")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity nicknameValidation(@Valid @RequestBody MemberNicknameValidationDto request) {
        memberService.validateNickname(request.nickname);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @ApiOperation(value = "FCM 토큰 등록")
    @PostMapping("/fcm-token")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity setFcmToken(@Valid @RequestBody MemberFcmTokenDto request) {
        memberService.setFcmToken(request.fcmToken);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @ApiOperation(value = "내 정보 조회")
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity me() {
        Member member = memberJwtService.getMember();
        MemberRetrieveDto memberRetrieveDto = memberMapper.memberToMemberRetrieveDto(member);
        return new ResponseEntity<>(memberRetrieveDto, HttpStatus.OK);
    }

//    @GetMapping("/{memberId}") // todo : 관리자 권한 있어야 실행 가능한 것으로 바꾸기
//    public ResponseEntity getMember(
//            @RequestHeader(value = "Authorization", required = false) String accessToken,
//            @PathVariable(required = false) Long memberId) {
//        MemberResponseDto memberResponseDto = memberMapper.memberToMemberResponseDto(memberService.getMember(memberId));
//        return ResponseEntity.status(HttpStatus.OK).body(memberResponseDto);
//    }

//    @ApiOperation(
//            value = "통계 조회",
//            notes = " - Authorization 토큰 필수\n" +
//                    " - month=2023-10(yyyy-mm형식)\n")
//    @GetMapping("/statistics")
//    public ResponseEntity getStatistics(
//            @RequestHeader(value = "Authorization", required = false) String accessToken,
//            @RequestParam(required = false) String month) {
//        if (ObjectUtils.isEmpty(accessToken)) {
//            throw new BusinessException(ErrorCode.MISSING_REQUEST);
//        }
//        Member member = jwtService.getMemberFromAccessToken(accessToken);
//        List<MemberStatisticsResponseDto> memberStatisticsResponseDtoList = memberService.getMemberStatistics(member, month);
//        return ResponseEntity.status(HttpStatus.OK).body(memberStatisticsResponseDtoList);
//    }

//    @ApiOperation(
//            value = "월별 통계 여부 조회",
//            notes = " - Authorization 토큰 필수")
//    @GetMapping("/year-statistics")
//    public ResponseEntity getYearStatistics(
//            @RequestHeader(value = "Authorization", required = false) String accessToken) {
//        if (ObjectUtils.isEmpty(accessToken)) {
//            throw new BusinessException(ErrorCode.MISSING_REQUEST);
//        }
//        Member member = jwtService.getMemberFromAccessToken(accessToken);
//        List<MemberYearStatisticsResponseDto> memberStatisticsResponseDtoList = memberService.getMemberYearStatistics(member);
//        return ResponseEntity.status(HttpStatus.OK).body(memberStatisticsResponseDtoList);
//    }
//
//    @ApiOperation(
//            value = "My Page - [닉네임, 프로필, 내 티켓, 통계 보기, 좋아요한 티켓 수]",
//            notes = " - Authorization 토큰 필수")
//    @GetMapping("/my-page")
//    public ResponseEntity getMyPage(
//            @RequestHeader(value = "Authorization", required = false) String accessToken) {
//        if (ObjectUtils.isEmpty(accessToken)) {
//            throw new BusinessException(ErrorCode.MISSING_REQUEST);
//        }
//        Member member = jwtService.getMemberFromAccessToken(accessToken);
//        MemberMyPageResponseDto memberMyPageResponseDto = memberService.getMyPage(member);
//        return ResponseEntity.status(HttpStatus.OK).body(memberMyPageResponseDto);
//    }
//
//    @ApiOperation(
//            value = "Member 조회 & 닉네임 조회(중복 검사)",
//            notes = "1. AccessToken으로 조회할 경우 : Header의 Authorization에 accessToken을 넣어주세요.\n" +
//                    "2. socialId와 socialType으로 조회할 경우 : ?socialId=abcdefg&socialType=KAKAO\n" +
//                    "3. nickname으로 조회할 경우 : ?nickname=닉네임입력" +
//                    "" +
//                    " - 해당 Member 없을 경우 -> 400에러, code : U001, message : 사용자를 찾을 수 없습니다.\n" +
//                    " - socialType은 KAKAO와 APPLE만 가능합니다.")
//    @GetMapping
//    public ResponseEntity getMember(
//            @RequestHeader(value = "Authorization", required = false) String accessToken,
//            @RequestParam(required = false) String socialId,
//            @RequestParam(required = false) SocialType socialType,
//            @RequestParam(required = false) String nickname) {
//
//        if (nickname != null) {
//            memberService.validateNickname(nickname);
//            return ResponseEntity.status(HttpStatus.OK).body(MemberNicknameResponseDto.builder().message("닉네임을 사용할 수 있습니다.").nickname(nickname).build());
//        }
//
//
//        Member member;
//        if (accessToken != null) {
//            member = jwtService.getMemberFromAccessToken(accessToken);
//        } else if (socialId != null && socialType != null) {
//            member = memberService.getMemberBySocialIdAndSocialType(socialId, socialType);
//        } else {
//            throw new BusinessException(ErrorCode.INVALID_REQUEST);
//        }
//        MemberResponseDto memberResponseDto = memberMapper.memberToMemberResponseDto(member);
//        memberResponseDto.setCategorys(member.getOnboardingMemberCategories().stream().map(c -> c.getCategory().getName()).collect(Collectors.toList()));
//        return ResponseEntity.status(HttpStatus.OK).body(memberResponseDto);
//    }
//
//    @GetMapping("/list")
//    public ResponseEntity getMemberList(
//            @RequestHeader(value = "Authorization", required = false) String accessToken) {
//        if (ObjectUtils.isEmpty(accessToken)) {
//            throw new BusinessException(ErrorCode.MISSING_REQUEST);
//        }
//        List<MemberResponseDto> memberResponseDtoList = memberMapper.membersToMemberResponseDtos(memberService.getMemberList());
//        return ResponseEntity.status(HttpStatus.OK).body(memberResponseDtoList);
//    }
//
//    @ApiOperation(value = "회원 탈퇴")
//    @DeleteMapping
//    public ResponseEntity deleteMember(
//            @RequestHeader(value = "Authorization", required = false) String accessToken) {
//        if (ObjectUtils.isEmpty(accessToken)) {
//            throw new BusinessException(ErrorCode.MISSING_REQUEST);
//        }
//        memberService.deleteMember(jwtService.getMemberFromAccessToken(accessToken).getId());
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
//    }
//
//    @ApiOperation(value = "로그아웃")
//    @GetMapping("/logout")
//    public ResponseEntity logoutMember(
//            @RequestHeader(value = "Authorization", required = false) String accessToken) { // todo : header 안 넣으면 나오는 에러 문구 수정하기
//        if (ObjectUtils.isEmpty(accessToken)) {
//            throw new BusinessException(ErrorCode.MISSING_REQUEST);
//        }
//        logoutTokenService.memberLogout(accessToken);
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
//    }
}
