package project.backend.domain.culturalevent.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.backend.domain.category.entity.Category;
import project.backend.domain.common.entity.BaseEntity;
import project.backend.domain.culturalevnetcategory.entity.CulturalEventCategory;
import project.backend.domain.place.entity.Place;
import project.backend.domain.ticketingsite.entity.TicketingSite;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CulturalEvent extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name = "title")
    public String title;

    @Column(name = "thumbnailImageUrl")
    public String thumbnailImageUrl;

    @Column(name = "startDate")
    public Date startDate;

    @Column(name = "endDate")
    public Date endDate;

    @Column(name = "ticketOpenDate")
    public Date ticketOpenDate;

    @Column(name = "bookingOpenDate")
    public Date bookingOpenDate;

    @Column(name = "runningTime")
    public String runningTime;

    @Column(name = "summary")
    public String summary;

    @Column(name = "genre")
    public String genre;

    @Column(name = "information")
    public String information;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "culturalEventCategoryId")
    public CulturalEventCategory culturalEventCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place")
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
}
