package project.backend.domain.member.dto;

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
public class MemberResponseDto {
    public Long id;
    public String nickname;
    public String profileImageUrl;
    public Agree marketingAgree;
    public Agree pushAgree;
    public LocalDateTime createdDate;
    public LocalDateTime updatedDate;
    public List<String> categorys;
}
