package project.backend.domain.culturalevent.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.backend.domain.common.entity.BaseEntity;
import project.backend.domain.culturaleventevalutaion.entity.CulturalEventEvaluation;
import project.backend.domain.like.entity.CulturalEventLike;
import project.backend.domain.culturalevnetcategory.entity.CulturalEventCategory;
import project.backend.domain.culturalevnetinfo.entity.CulturalEventInfo;
import project.backend.domain.member.entity.Member;
import project.backend.domain.place.entity.Place;
import project.backend.domain.ticketingsite.entity.TicketingSite;
import project.backend.domain.visit.entity.CulturalEventVisit;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "cultural_event", indexes = {
        @Index(name = "idx_title", columnList = "title")
})
public class CulturalEvent extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public String title;

    public String thumbnailImageUrl;

    public Date startDate;

    public Date endDate;

    public LocalDateTime ticketOpenDate;

    public String runningTime;

    public String summary;

    public String genre;

    public String viewRateName;

    public Integer likeCount = 0;

    public Integer visitCount = 0;

    public Integer point = 0;

    @Column(columnDefinition = "TEXT")
    public String information;

    @Column(columnDefinition = "TEXT")
    public String topic;

    @Column(columnDefinition = "TEXT")
    public String sentiment;

    @ManyToOne(fetch = FetchType.LAZY)
    public CulturalEventCategory culturalEventCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    public Place place;

    @OneToMany(mappedBy = "culturalEvent", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public List<TicketingSite> ticketingSiteList = new ArrayList<>();

    @OneToMany(mappedBy = "culturalEvent", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public List<CulturalEventEvaluation> culturalEventEvaluationList = new ArrayList<>();

    @OneToMany(mappedBy = "culturalEvent", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public List<CulturalEventLike> culturalEventLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "culturalEvent", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public List<CulturalEventVisit> culturalEventVisitList = new ArrayList<>();

    @OneToMany(mappedBy = "culturalEvent", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public List<CulturalEventInfo> culturalEvnetInfoList = new ArrayList<>();

    @Builder
    public CulturalEvent(
            String title,
            String thumbnailImageUrl,
            Date startDate,
            Date endDate,
            LocalDateTime ticketOpenDate,
            String runningTime,
            String summary,
            String genre,
            String information,
            String viewRateName
    ) {
        this.title = title;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        this.ticketOpenDate = ticketOpenDate;
        this.runningTime = runningTime;
        this.summary = summary;
        this.genre = genre;
        this.information = information;
        this.viewRateName = viewRateName;
    }


    // == 연관관계 매핑 == //
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

    public Optional<CulturalEventLike> findMemberLike(Member member) {
        return this.getCulturalEventLikeList().stream().filter(culturalEventLike -> culturalEventLike.member == member).findFirst();
    }

    public Optional<CulturalEventVisit> findMemberVisit(Member member) {
        return this.getCulturalEventVisitList().stream().filter(culturalEventVisit -> culturalEventVisit.member == member).findFirst();
    }

    public void increaseLikeCount() {
        this.likeCount = this.likeCount + 1;
        this.point = this.point + 2;
    }

    public void decreaseLikeCount() {
        this.likeCount = this.likeCount - 1;
        this.point = this.point - 2;
    }

    public void increaseVisitCount() {
        this.visitCount = this.visitCount + 1;
        this.point = this.point + 1;
    }
}
