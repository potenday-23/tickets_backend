package project.backend.domain.culturalevent.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.backend.domain.category.entity.Category;
import project.backend.domain.common.entity.BaseEntity;
import project.backend.domain.culturalevnetcategory.entity.CulturalEventCategory;
import project.backend.domain.member.dto.MemberPatchRequestDto;
import project.backend.domain.member.entity.Member;
import project.backend.domain.place.entity.Place;
import project.backend.domain.ticketingsite.entity.TicketingSite;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CulturalEvent extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String title;

    public String thumbnailImageUrl;

    public Date startDate;

    public Date endDate;

    public Date ticketOpenDate;

    public Date bookingOpenDate;

    public String runningTime;

    public String summary;

    public String genre;

    @Column(columnDefinition = "TEXT")
    public String information;

    @ManyToOne(fetch = FetchType.LAZY)
    public CulturalEventCategory culturalEventCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    public Place place;

    @OneToMany(mappedBy = "culturalEvent", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public List<TicketingSite> culturalEvents = new ArrayList<>();

    @Builder
    public CulturalEvent(
            String title,
            String thumbnailImageUrl,
            Date startDate,
            Date endDate,
            Date ticketOpenDate,
            Date bookingOpenDate,
            String runningTime,
            String summary,
            String genre,
            String information
    ) {
        this.title = title;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        this.ticketOpenDate = ticketOpenDate;
        this.bookingOpenDate = bookingOpenDate;
        this.runningTime = runningTime;
        this.summary = summary;
        this.genre = genre;
        this.information = information;
    }

    public void setPlace(Place place) {
        if (place != null) {
            if (this.place != null) {
                if (this.place.getCulturalEvents().contains(this)) {
                    this.place.getCulturalEvents().remove(this);
                }
            }
            this.place = Optional.ofNullable(place).orElse(this.place);
            this.place.getCulturalEvents().add(this);
        }
    }

    public void setCulturalEventCategory(CulturalEventCategory culturalEventCategory) {
        if (culturalEventCategory != null) {
            if (this.culturalEventCategory != null) {
                if (this.culturalEventCategory.getCulturalEvents().contains(this)) {
                    this.culturalEventCategory.getCulturalEvents().remove(this);
                }
            }
            this.culturalEventCategory = Optional.ofNullable(culturalEventCategory).orElse(this.culturalEventCategory);
            this.culturalEventCategory.getCulturalEvents().add(this);

        }
    }
}
