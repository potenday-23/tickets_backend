package project.backend.domain.culturalevent.dto;

import lombok.*;
import project.backend.domain.member.entity.Member;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

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
    private Boolean isLiked;
    private Boolean isOpened;

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