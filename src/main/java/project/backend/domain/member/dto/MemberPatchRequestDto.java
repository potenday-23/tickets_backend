package project.backend.domain.member.dto;

import lombok.*;
import project.backend.domain.member.entity.SocialType;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberPatchRequestDto {
    @Size(min=1, max=10, message = "1~10자 이내로 입력해주세요.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9-_ ]*$", message = "한 글자(ㄱ) 또는 특수문자를 사용할 수 없어요.")
    public String nickname;
    public String profileUrl;
    public String refreshToken;
}
