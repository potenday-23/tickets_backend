package project.backend.domain.ticketingsite.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.backend.domain.common.entity.BaseEntity;
import project.backend.domain.culturalevent.entity.CulturalEvent;
import project.backend.domain.culturalevnetcategory.entity.CulturalEventCategory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TicketingSite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Enumerated(value = EnumType.STRING)
    public Platform platform;

    public String link;

    @ManyToOne(fetch = FetchType.LAZY)
    public CulturalEvent culturalEvent;

    @Builder
    public TicketingSite(Platform platform, String link) {
        this.platform = platform;
        this.link = link;
    }

    // == 연관관계 매핑 == //
    public void setCulturalEvent(CulturalEvent culturalEvent) {
        if (this.culturalEvent != null) {
            if (this.culturalEvent.getTicketingSiteList().contains(this)) {
                this.culturalEvent.getTicketingSiteList().remove(this);
            }
        }
        this.culturalEvent = Optional.ofNullable(culturalEvent).orElse(this.culturalEvent);
        this.culturalEvent.getTicketingSiteList().add(this);
    }
}
