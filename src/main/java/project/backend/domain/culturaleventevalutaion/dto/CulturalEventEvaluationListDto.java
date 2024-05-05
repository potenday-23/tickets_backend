package project.backend.domain.culturaleventevalutaion.dto;

import lombok.*;
import project.backend.domain.culturalevent.entity.CulturalEvent;
import project.backend.domain.culturaleventevalutaion.entity.EvaluationType;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CulturalEventEvaluationListDto {
    private String title;
    private String content;
    private Integer score;
    private EvaluationType type;
    private Long culturalEventId;
}
