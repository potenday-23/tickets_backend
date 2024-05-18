package project.backend.domain.culturalevent.dto;

import lombok.*;
import project.backend.domain.member.entity.Member;
import project.backend.domain.place.dto.PlaceRetrieveDto;
import project.backend.domain.ticketingsite.dto.TicketingSiteListDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
    private Integer likeCount;
    private Integer visitCount;
    private Boolean isLiked;
    private Boolean isOpened;
    private List<String> informationList;

    public void setIsLiked(Member member) {
        if (member == null) {
            this.isLiked = false;
        } else {
            this.isLiked = member.getCulturalEventLikeList().stream().anyMatch(like -> Objects.equals(like.culturalEvent.id, this.id));
        }
    }

    public void setIsOpened() {
        LocalDateTime now = LocalDateTime.now();
        this.isOpened = !now.isBefore(this.ticketOpenDate);
    }
}