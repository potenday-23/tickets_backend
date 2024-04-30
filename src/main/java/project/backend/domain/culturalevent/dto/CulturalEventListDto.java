package project.backend.domain.culturalevent.dto;
import lombok.*;
import project.backend.domain.culturalevent.entity.CulturalEventStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CulturalEventListDto {
    private Long id;
    private String thumbnailImageUrl;
    private String categoryName;
    private String title;
    private String placeName;
    private Date startDate;
    private Date endDate;
    private LocalDateTime ticketOpenDate;
    private Boolean isLiked; // TODO : 구현 이전
    private Boolean isOpened;
}