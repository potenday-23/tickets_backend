package project.backend.domain.culturalevent.dto;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CulturalEventRetrieveDto {
    private String title;
    private String thumbnailImageUrl;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime ticketOpenDate;
    private String runningTime;
    private String summary;
    private String genre;
    private String information;
}