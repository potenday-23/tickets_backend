package project.backend.domain.member.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;
import project.backend.domain.member.entity.SocialType;
import project.backend.global.annotation.SocialTypeSubset;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginDto {
    @NotNull(message = "socialId는 필수값입니다.")
    public String socialId;

    @NotNull(message = "socialType은 필수값입니다.")
    @SocialTypeSubset(anyOf = {SocialType.KAKAO, SocialType.APPLE, SocialType.GOOGLE}, message = "KAKAO, APPLE, GOOGLE 중 하나를 입력해야 합니다.")
    public SocialType socialType;

    @Email(message = "유효한 이메일 형식을 입력해야 합니다.")
    public String email;
}