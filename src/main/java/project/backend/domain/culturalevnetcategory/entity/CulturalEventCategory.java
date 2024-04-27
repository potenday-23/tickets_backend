package project.backend.domain.culturalevnetcategory.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.backend.domain.common.entity.BaseEntity;
import project.backend.domain.culturalevent.entity.CulturalEvent;
import project.backend.domain.ticket.entity.Ticket;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CulturalEventCategory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String title;

    public String ordering;

    public String imageUrl;

    @OneToMany(mappedBy = "culturalEventCategory", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public List<CulturalEvent> culturalEvents = new ArrayList<>();

    @Builder
    public CulturalEventCategory(String title, String ordering, String imageUrl) {
        this.title = title;
        this.ordering = ordering;
        this.imageUrl = imageUrl;
    }
}
