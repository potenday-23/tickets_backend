package project.backend.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import project.backend.domain.member.entity.Agree;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberRetrieveDto {
    public Long id;
    public String email;
    public String nickname;
    public LocalDate birthday;
    public Boolean isMarketingAgree;
    public Boolean isSignup;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String accessToken;
}
