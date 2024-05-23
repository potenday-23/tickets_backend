package project.backend.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import project.backend.domain.member.entity.SocialType;
import project.backend.global.annotation.ValidEnum;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginDto {
    @NotNull(message = "socialId는 필수값입니다.")
    @Schema(description = "소셜 아이디", example = "3149019856", required = true)
    public String socialId;

    @NotNull(message = "socialType은 필수값입니다.")
    @Schema(description = "소셜 유형", example = "KAKAO", required = true)
    public SocialType socialType;

    @Email(message = "유효한 이메일 형식을 입력해야 합니다.")
    @Schema(description = "이메일", example = "ticats@gmail.com")
    public String email;
}