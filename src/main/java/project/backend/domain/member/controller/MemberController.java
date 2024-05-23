package project.backend.domain.member.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.backend.domain.category.service.CategoryService;
import project.backend.domain.jwt.service.JwtService;
import project.backend.domain.member.dto.*;
import project.backend.domain.member.entity.Member;
import project.backend.domain.member.entity.SocialType;
import project.backend.domain.member.mapper.MemberMapper;
import project.backend.domain.member.service.LogoutTokenService;
import project.backend.domain.member.service.MemberService;
import project.backend.global.error.exception.BusinessException;
import project.backend.global.error.exception.ErrorCode;
import project.backend.global.s3.service.ImageService;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Api(tags = "B. 멤버 API")
public class MemberController {

    private final MemberService memberService;
    private final MemberMapper memberMapper;
    private final JwtService jwtService;
    private final ImageService imageService;
    private final LogoutTokenService logoutTokenService;
    private final CategoryService categoryService;

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

    @ApiOperation(value = "회원가입 정보 작성")
    @PostMapping("/signup")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity signup(@Valid @RequestBody MemberSignupDto request) {
        // TODO : 닉네임 중복 체크
        Member member = memberService.setMemberSignup(request);

        // 응답
        MemberRetrieveDto memberRetrieveDto = memberMapper.memberToMemberRetrieveDto(member);
        return new ResponseEntity<>(memberRetrieveDto, HttpStatus.OK);
    }

    @ApiOperation(value = "닉네임 유효성 검사")
    @PostMapping("/nickname-validation")
    public ResponseEntity nicknameValidation(@Valid @RequestBody MemberNicknameValidationDto request) {
        memberService.verifiedNickname(request.nickname);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @ApiIgnore
    @GetMapping("/{memberId}") // todo : 관리자 권한 있어야 실행 가능한 것으로 바꾸기
    public ResponseEntity getMember(
            @RequestHeader(value = "Authorization", required = false) String accessToken,
            @PathVariable(required = false) Long memberId) {
        MemberResponseDto memberResponseDto = memberMapper.memberToMemberResponseDto(memberService.getMember(memberId));
        return ResponseEntity.status(HttpStatus.OK).body(memberResponseDto);
    }

    @ApiIgnore
    @ApiOperation(
            value = "통계 조회",
            notes = " - Authorization 토큰 필수\n" +
                    " - month=2023-10(yyyy-mm형식)\n")
    @GetMapping("/statistics")
    public ResponseEntity getStatistics(
            @RequestHeader(value = "Authorization", required = false) String accessToken,
            @RequestParam(required = false) String month) {
        if (ObjectUtils.isEmpty(accessToken)) {
            throw new BusinessException(ErrorCode.MISSING_REQUEST);
        }
        Member member = jwtService.getMemberFromAccessToken(accessToken);
        List<MemberStatisticsResponseDto> memberStatisticsResponseDtoList = memberService.getMemberStatistics(member, month);
        return ResponseEntity.status(HttpStatus.OK).body(memberStatisticsResponseDtoList);
    }

    @ApiIgnore
    @ApiOperation(
            value = "월별 통계 여부 조회",
            notes = " - Authorization 토큰 필수")
    @GetMapping("/year-statistics")
    public ResponseEntity getYearStatistics(
            @RequestHeader(value = "Authorization", required = false) String accessToken) {
        if (ObjectUtils.isEmpty(accessToken)) {
            throw new BusinessException(ErrorCode.MISSING_REQUEST);
        }
        Member member = jwtService.getMemberFromAccessToken(accessToken);
        List<MemberYearStatisticsResponseDto> memberStatisticsResponseDtoList = memberService.getMemberYearStatistics(member);
        return ResponseEntity.status(HttpStatus.OK).body(memberStatisticsResponseDtoList);
    }

    @ApiIgnore
    @ApiOperation(
            value = "My Page - [닉네임, 프로필, 내 티켓, 통계 보기, 좋아요한 티켓 수]",
            notes = " - Authorization 토큰 필수")
    @GetMapping("/my-page")
    public ResponseEntity getMyPage(
            @RequestHeader(value = "Authorization", required = false) String accessToken) {
        if (ObjectUtils.isEmpty(accessToken)) {
            throw new BusinessException(ErrorCode.MISSING_REQUEST);
        }
        Member member = jwtService.getMemberFromAccessToken(accessToken);
        MemberMyPageResponseDto memberMyPageResponseDto = memberService.getMyPage(member);
        return ResponseEntity.status(HttpStatus.OK).body(memberMyPageResponseDto);
    }

    @ApiIgnore
    @ApiOperation(
            value = "Member 조회 & 닉네임 조회(중복 검사)",
            notes = "1. AccessToken으로 조회할 경우 : Header의 Authorization에 accessToken을 넣어주세요.\n" +
                    "2. socialId와 socialType으로 조회할 경우 : ?socialId=abcdefg&socialType=KAKAO\n" +
                    "3. nickname으로 조회할 경우 : ?nickname=닉네임입력" +
                    "" +
                    " - 해당 Member 없을 경우 -> 400에러, code : U001, message : 사용자를 찾을 수 없습니다.\n" +
                    " - socialType은 KAKAO와 APPLE만 가능합니다.")
    @GetMapping
    public ResponseEntity getMember(
            @RequestHeader(value = "Authorization", required = false) String accessToken,
            @RequestParam(required = false) String socialId,
            @RequestParam(required = false) SocialType socialType,
            @RequestParam(required = false) String nickname) {

        if (nickname != null) {
            memberService.verifiedNickname(nickname);
            return ResponseEntity.status(HttpStatus.OK).body(MemberNicknameResponseDto.builder().message("닉네임을 사용할 수 있습니다.").nickname(nickname).build());
        }


        Member member;
        if (accessToken != null) {
            member = jwtService.getMemberFromAccessToken(accessToken);
        } else if (socialId != null && socialType != null) {
            member = memberService.getMemberBySocialIdAndSocialType(socialId, socialType);
        } else {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }
        MemberResponseDto memberResponseDto = memberMapper.memberToMemberResponseDto(member);
        memberResponseDto.setCategorys(member.getOnboardingMemberCategories().stream().map(c -> c.getCategory().getName()).collect(Collectors.toList()));
        return ResponseEntity.status(HttpStatus.OK).body(memberResponseDto);
    }

    @ApiIgnore
    @GetMapping("/list")
    public ResponseEntity getMemberList(
            @RequestHeader(value = "Authorization", required = false) String accessToken) {
        if (ObjectUtils.isEmpty(accessToken)) {
            throw new BusinessException(ErrorCode.MISSING_REQUEST);
        }
        List<MemberResponseDto> memberResponseDtoList = memberMapper.membersToMemberResponseDtos(memberService.getMemberList());
        return ResponseEntity.status(HttpStatus.OK).body(memberResponseDtoList);
    }

    @ApiIgnore
    @ApiOperation(
            value = "닉네임 & 프로필 이미지 등록, 온보딩 기능",
            notes = " - 닉네임 변경 원할 시 : request -> {\"nickname\" : \"가방이\", \"marketingAgree\":\"AGREE\", \"pushAgree\":\"DISAGREE\"}\n" +
                    " - 프로필 이미지 변경 원할 시 : profileImage -> MultipartFile으로 파일 입력 \n" +
                    " - 온보딩 입력 & 수정 원힐 시 : categorys -> [\"기타\", \"영화\"] \n" +
                    " - 닉네임과 온보딩 입력란은 application/json형식으로 요청해주세요.(swagger에서는 작동하지 않습니다.)" +
                    " - Header의 AccessToken만 필수 값이고, 나머지는 필수 값이 아님")
    @RequestMapping(method = RequestMethod.PATCH, consumes = "multipart/form-data")
    public ResponseEntity patchMember(
            @RequestHeader(value = "Authorization", required = false) String accessToken,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            @Valid @RequestPart(value = "request", required = false) MemberPatchRequestDto request,
            @RequestPart(value = "categorys", required = false) List<String> categorys
    ) {
        if (ObjectUtils.isEmpty(accessToken)) {
            throw new BusinessException(ErrorCode.MISSING_REQUEST);
        }

        memberService.verifiedNickname(request.getNickname());
        Member member = jwtService.getMemberFromAccessToken(accessToken);
        if (request == null) {
            request = MemberPatchRequestDto.builder().build();
        }
        if (profileImage != null) {
            request.setProfileUrl(imageService.updateImage(profileImage, "Member", "profileUrl"));
        }
        if (categorys != null) {
            categorys = categorys.stream().distinct().collect(Collectors.toList());
            memberService.onboardingMember(member.id, categorys);
        } else {
            categorys = categoryService.getCategoryList().stream().map(categoryResponseDto -> categoryResponseDto.name).collect(Collectors.toList());
        }
        MemberResponseDto memberResponseDto = memberMapper.memberToMemberResponseDto(memberService.patchMember(member.getId(), request));
        memberResponseDto.setCategorys(member.getOnboardingMemberCategories().stream().map(c -> c.getCategory().getName()).collect(Collectors.toList()));
        return ResponseEntity.status(HttpStatus.OK).body(memberResponseDto);
    }

    @ApiIgnore
    @ApiOperation(value = "회원 탈퇴")
    @DeleteMapping
    public ResponseEntity deleteMember(
            @RequestHeader(value = "Authorization", required = false) String accessToken) {
        if (ObjectUtils.isEmpty(accessToken)) {
            throw new BusinessException(ErrorCode.MISSING_REQUEST);
        }
        memberService.deleteMember(jwtService.getMemberFromAccessToken(accessToken).getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @ApiIgnore
    @ApiOperation(value = "로그아웃")
    @GetMapping("/logout")
    public ResponseEntity logoutMember(
            @RequestHeader(value = "Authorization", required = false) String accessToken) { // todo : header 안 넣으면 나오는 에러 문구 수정하기
        if (ObjectUtils.isEmpty(accessToken)) {
            throw new BusinessException(ErrorCode.MISSING_REQUEST);
        }
        logoutTokenService.memberLogout(accessToken);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
