package project.backend.domain.jwt.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.backend.domain.jwt.dto.KakaoUserInfo;
import project.backend.domain.jwt.response.JwtResponse;
import project.backend.domain.jwt.response.TokenResponse;
import project.backend.domain.jwt.service.JwtService;
import project.backend.domain.member.dto.MemberResponseDto;
import project.backend.domain.member.entity.Member;
import project.backend.domain.member.mapper.MemberMapper;
import project.backend.domain.member.service.MemberService;
import project.backend.global.error.exception.BusinessException;
import project.backend.global.error.exception.ErrorCode;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "카테고리 API", description = "Swagger 테스트용 API")
@Validated
@AllArgsConstructor
@Slf4j
public class JwtController {

    private final MemberService memberService;
    private final JwtService jwtService;
    private final MemberMapper memberMapper;

    @Operation(
            summary = "로그인 & 회원가입 기능(로그인, 회원가입 로직을 따로 나누지 않습니다.)",
            description = " - 다음의 링크에서 사용 방법을 확인해 주세요 : https://www.notion.so/sideproject-unione/c9c895d9cc714cee89734d268560fe52")
    @GetMapping("/kakao/login")
    public ResponseEntity login(@RequestParam(value = "token", required = false) String token,
                                @RequestParam(value = "code", required = false) String code,
                                @RequestParam(value = "redirect_url", required = false) String redirect_url) {

        // 유저 정보 얻기
        if(code != null) {
            if (redirect_url == null){
                throw new BusinessException(ErrorCode.MISSING_REDIRECT_REQUEST_PARAM);
            }
            token = jwtService.getKakaoAccessToken(code, redirect_url);
        } else {
            throw new BusinessException(ErrorCode.MISSING_REQUEST_PARAM);
        }
        KakaoUserInfo kakaoUserInfo = jwtService.getKakaoUserInfo(token);

        // 해당 kakao ID를 가진 Member 반환
        Member member = memberService.findMemberBySocialId(kakaoUserInfo.getKakaoId(), kakaoUserInfo.getProfileUrl());

        // accessToken과 refreshToken발급
        String accessToken = jwtService.getAccessToken(member); // 에러 발생
        String refreshToken = member.getRefreshToken();

        // 응답
        MemberResponseDto memberResponse = memberMapper.memberToMemberResponseDto(member);
        TokenResponse tokenResponse = TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken).build();
        JwtResponse jwtResponse = JwtResponse.builder()
                .token(tokenResponse)
                .member(memberResponse).build();
        return new ResponseEntity<>(jwtResponse, HttpStatus.CREATED);
    }

}