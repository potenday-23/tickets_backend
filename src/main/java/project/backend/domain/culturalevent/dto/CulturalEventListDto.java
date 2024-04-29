package project.backend.domain.culturalevent.dto;
import lombok.*;
import project.backend.domain.culturalevent.entity.CulturalEventStatus;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CulturalEventListDto {
    private Long id;
    private String thumbnailImageUrl;
    private String categoryTitle; // 없는 것
    private String title;
    private String placeTitle; // 없는 것
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isLiked; // 없는 것
    private CulturalEventStatus status; // 없는 것
}