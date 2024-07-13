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
}
