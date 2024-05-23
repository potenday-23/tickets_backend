package project.backend.domain.jwt.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.backend.domain.category.dto.CategoryResponseDto;
import project.backend.domain.category.entity.Category;
import project.backend.domain.category.repository.CategoryRepository;
import project.backend.domain.category.service.CategoryService;
import project.backend.domain.jwt.dto.JwtRequestDto;
import project.backend.domain.jwt.response.JwtResponse;
import project.backend.domain.jwt.response.TokenResponse;
import project.backend.domain.jwt.service.JwtService;
import project.backend.domain.member.dto.MemberPatchRequestDto;
import project.backend.domain.member.dto.MemberResponseDto;
import project.backend.domain.member.entity.Member;
import project.backend.domain.member.mapper.MemberMapper;
import project.backend.domain.member.service.MemberService;
import project.backend.global.error.exception.BusinessException;
import project.backend.global.error.exception.ErrorCode;
import project.backend.global.s3.service.ImageService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@Api(tags = "로그인 API")
@Validated
@AllArgsConstructor
@Slf4j
public class JwtController {

    private final MemberService memberService;
    private final JwtService jwtService;
    private final MemberMapper memberMapper;
    private final ImageService imageService;
    private final CategoryRepository categoryRepository;


    @ApiOperation(
            value = "회원가입 & 로그인",
            notes = " - request : {\"socialId\" :\"abcde2\", \"socialType\":\"KAKAO\", \"profileUrl\":\"url주소\", \"nickname\" : \"가방이\", \"marketingAgree\":\"AGREE\", \"pushAgree\":\"DISAGREE\"}\n" +
                    " - profileImage : MultipartFile형식\n" +
                    " - categorys : [\"영화\", \"뮤지컬\"]\n" +
                    "1. request의 socialId, socialType은 필수\n" +
                    "2. profileUrl과 profileImage 파일이 동시에 있을 경우, profileImage가 등록됩니다.\n" +
                    "3. marketingAgree와 pushAgree는 필수 아님. default 값은 DISAGREE\n" +
                    "4. request와 categorys는 application/json 형식으로 요청 요망")
    @PostMapping("/login")
    public ResponseEntity login(
            @Valid @RequestPart(required = false) JwtRequestDto request,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestPart(value = "categorys", required = false) List<String> categorys) {

        if (ObjectUtils.isEmpty(request)){
            throw new BusinessException(ErrorCode.MISSING_REQUEST);
        }

        // 닉네임 중복 검사
        memberService.verifiedNickname(request.nickname);

        // socialId, socialType기준 Member 반환, 없다면 새로 생성
        Member member = memberService.getMemberBySocial(request.socialId, request.socialType, "offbeat1020@naver.com");

        // profile Url 설정
        if (profileImage != null) {
            request.setProfileUrl(imageService.updateImage(profileImage, "Member", "profileUrl"));
        }

        // nickname, profileUrl, marketingAgree, pushAgree 설정
        memberService.patchMember(member.getId(), MemberPatchRequestDto.builder()
                                                                        .nickname(request.nickname)
                                                                        .profileUrl(request.profileUrl)
                                                                        .marketingAgree(request.marketingAgree)
                                                                        .pushAgree(request.pushAgree).build());

        // 온보딩 카테고리 설정
        if (categorys != null) {
            categorys = categorys.stream().distinct().collect(Collectors.toList());
        } else {
            categorys = categoryRepository.findAll().stream().map(Category::getName).collect(Collectors.toList());
        }
        memberService.onboardingMember(member.id, categorys);

        // accessToken과 refreshToken 발급
        String accessToken = jwtService.getAccessToken(member);
        String refreshToken = member.getRefreshToken();

        // 응답
        MemberResponseDto memberResponse = memberMapper.memberToMemberResponseDto(member);
        memberResponse.setCategorys(member.getOnboardingMemberCategories().stream().map(c -> c.getCategory().getName()).collect(Collectors.toList()));
        TokenResponse tokenResponse = TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken).build();
        JwtResponse jwtResponse = JwtResponse.builder()
                .token(tokenResponse)
                .member(memberResponse).build();
        return new ResponseEntity<>(jwtResponse, HttpStatus.CREATED);
    }
}