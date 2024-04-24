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

@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TicketingSite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name = "platform")
    public String platform;

    @Column(name = "link")
    public String link;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "culturalEventId")
    public CulturalEvent culturalEvent;

    @Builder
    public TicketingSite(String platform, String link) {
        this.platform = platform;
        this.link = link;
    }
}
