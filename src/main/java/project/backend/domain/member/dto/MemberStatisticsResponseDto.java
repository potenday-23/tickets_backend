package project.backend.domain.member.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberStatisticsResponseDto {
    private String category;
    private Long categoryCnt;
    private Double categoryPercent;
}
