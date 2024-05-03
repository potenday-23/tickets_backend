package project.backend.domain.culturalevent.dto;

import lombok.*;
import project.backend.domain.culturalevent.service.CulturalEventService;
import project.backend.domain.culturalevnetinfo.dto.CulturalEventInfoResponseDto;
import project.backend.domain.culturalevnetinfo.entity.CulturalEventInfo;
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
    private final CulturalEventService culturalEventService;

    private Long id;
    private String title;
    private String thumbnailImageUrl;
    private String categoryName;
    private PlaceRetrieveDto place;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime ticketOpenDate;
    private List<TicketingSiteListDto> ticketingSiteList;
    // TODO : 예매 사이트, 시놉시스 추가해야함
    private String runningTime;
    private String summary;
    private String genre;
    private List<String> informationList;
}