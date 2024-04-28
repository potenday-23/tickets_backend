package project.backend.domain.culturalevent.dto;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CulturalEventResponseDto {
    private String title;
    private String thumbnailImageUrl;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate ticketOpenDate;
    private LocalDate bookingOpenDate;
    private String runningTime;
    private String summary;
    private String genre;
    private String information;
}