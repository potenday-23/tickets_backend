package project.backend.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.backend.domain.jwt.service.JwtService;
import project.backend.domain.member.dto.MemberPostRequestDto;
import project.backend.domain.member.dto.MemberResponseDto;
import project.backend.domain.member.dto.MemberPatchRequestDto;
import project.backend.domain.member.entity.Member;
import project.backend.domain.member.mapper.MemberMapper;
import project.backend.domain.member.service.MemberService;
import project.backend.domain.onboardingmembercategory.entity.OnboardingMemberCategory;
import project.backend.global.s3.service.ImageService;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "카테고리 API", description = "Swagger 테스트용 API")
public class MemberController {

    private final MemberService memberService;
    private final MemberMapper memberMapper;
    private final JwtService jwtService;
    private final ImageService imageService;


    @PostMapping
    public ResponseEntity postMember(@RequestBody MemberPostRequestDto memberPostRequestDto) {
        Member member = memberService.createMember(memberPostRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberMapper.memberToMemberResponseDto(member));
    }

    @GetMapping("/{memberId}") // todo : 관리자 권한 있어야 실행 가능한 것으로 바꾸기
    public ResponseEntity getMember(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable Long memberId) {
        MemberResponseDto memberResponseDto = memberMapper.memberToMemberResponseDto(memberService.getMember(memberId));
        return ResponseEntity.status(HttpStatus.OK).body(memberResponseDto);
    }

    @GetMapping
    public ResponseEntity getMember(
            @RequestHeader("Authorization") String accessToken) {
        Member member = jwtService.getMemberFromAccessToken(accessToken);
        MemberResponseDto memberResponseDto = memberMapper.memberToMemberResponseDto(member);
        return ResponseEntity.status(HttpStatus.OK).body(memberResponseDto);
    }

    @GetMapping("/list")
    public ResponseEntity getMemberList(
            @RequestHeader("Authorization") String accessToken) {
        List<MemberResponseDto> memberResponseDtoList = memberMapper.membersToMemberResponseDtos(memberService.getMemberList());
        return ResponseEntity.status(HttpStatus.OK).body(memberResponseDtoList);
    }

    @Operation(
            summary = "닉네임 & 프로필 이미지 등록, 온보딩 기능",
            description = " - 닉네임 변경 원할 시 : request -> {\"nickname\" : \"가방이\"}\n" +
                    " - 프로필 이미지 변경 원할 시 : profileImage -> MultipartFile으로 파일 입력 \n" +
                    " - 온보딩 입력 & 수정 원힐 시 : categorys -> [\"기타\", \"영화\"] \n" +
                    " - 아직 중복 검사 로직은 없습니다!\n" +
                    " - 닉네임과 온보딩 입력란은 application/json형식으로 요청해주세요.(swagger에서는 작동하지 않습니다.)")
    @RequestMapping(method = RequestMethod.PATCH, consumes = "multipart/form-data")
    public ResponseEntity patchMember(
            @RequestHeader("Authorization") String accessToken,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            @Valid @RequestPart(value = "request", required = false) MemberPatchRequestDto request,
            @RequestPart(value = "categorys", required = false) List<String> categorys
            ) { // todo : 중복 검사 로직 작성하기
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
        }
        MemberResponseDto memberResponseDto = memberMapper.memberToMemberResponseDto(memberService.patchMember(member.getId(), request));
        memberResponseDto.setCategorys(member.getOnboardingMemberCategories().stream().map(c -> c.getCategory().getName()).collect(Collectors.toList()));
        return ResponseEntity.status(HttpStatus.OK).body(memberResponseDto);
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity deleteMember(@PathVariable Long memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
