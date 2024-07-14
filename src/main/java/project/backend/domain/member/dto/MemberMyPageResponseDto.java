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
public class MemberMyPageResponseDto {
    public Long id;
    public String nickname;
    public String profileImageUrl;
    public Integer myTicketCount;
    public String myStatistics;
    public Integer myLikeCount;
    public LocalDateTime createdDate;
    public LocalDateTime updatedDate;
}
