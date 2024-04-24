package project.backend.domain.culturalevent.dto;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CulturalEventResponseDto {
    private String title;
    private String thumbnailImageUrl;
    private Date startDate;
    private Date endDate;
    private Date ticketOpenDate;
    private Date bookingOpenDate;
    private String runningTime;
    private String summary;
    private String genre;
    private String information;
}