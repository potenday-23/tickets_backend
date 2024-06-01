package project.backend.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import project.backend.domain.member.entity.Gender;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberSignupDto {
    @NotNull(message = "nickname은 필수값입니다.")
    @Size(min = 2, max = 10, message = "닉네임은 최소 2글자에서 최대 10글자까지 작성이 가능해요.")
    @Pattern(regexp = "^[a-zA-Z가-힣]*$", message = "닉네임에는 영어, 한글만 사용이 가능해요.")
    @Schema(description = "닉네임", example = "티켓팅하는고양이", required = true)
    public String nickname;

    @Email(message = "유효한 이메일 형식을 입력해야 합니다.")
    @Size(max = 40, message = "이메일은 최대 40글자까지 작성이 가능해요.")
    @Schema(description = "이메일", example = "ticats@gmail.com", required = true)
    public String email;

    @NotNull(message = "birthday는 필수값입니다.")
    @Schema(description = "생년월일", example = "2000-10-20", required = true)
    public LocalDate birthday;

    @NotNull(message = "nickname은 필수값입니다.")
    @Schema(description = "성별", example = "FEMALE", required = true)
    public Gender gender;

    @Schema(description = "마케팅 정보 수신 및 이용 동의", example = "true", required = false)
    public Boolean isMarketingAgree;
}