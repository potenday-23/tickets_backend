package project.backend.domain.member.dto;

import lombok.*;
import project.backend.domain.member.entity.Agree;

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
    public Boolean isSignup;
    public String accessToken;
}
