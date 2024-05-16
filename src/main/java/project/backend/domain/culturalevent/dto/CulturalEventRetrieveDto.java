package project.backend.domain.culturalevent.dto;

import lombok.*;
import project.backend.domain.place.dto.PlaceRetrieveDto;
import project.backend.domain.ticketingsite.dto.TicketingSiteListDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CulturalEventRetrieveDto {
    private Long id;
    private String title;
    private String thumbnailImageUrl;
    private String categoryName;
    private PlaceRetrieveDto place;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime ticketOpenDate;
    private List<TicketingSiteListDto> ticketingSiteList;
    private String runningTime;
    private String summary;
    private String genre;
    private String viewRateName;
    private List<String> informationList;
}