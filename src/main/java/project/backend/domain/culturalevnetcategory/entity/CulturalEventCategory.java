package project.backend.domain.culturalevnetcategory.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.backend.domain.common.entity.BaseEntity;
import project.backend.domain.culturalevent.entity.CulturalEvent;
import project.backend.domain.member.entity.SocialType;
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

    @Enumerated(value = EnumType.STRING)
    public CategoryTitle title;

    public Integer ordering;

    public String imageUrl;

    @OneToMany(mappedBy = "culturalEventCategory", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public List<CulturalEvent> culturalEvents = new ArrayList<>();

    @Builder
    public CulturalEventCategory(CategoryTitle title, Integer ordering, String imageUrl) {
        this.title = title;
        this.ordering = ordering;
        this.imageUrl = imageUrl;
    }
}
