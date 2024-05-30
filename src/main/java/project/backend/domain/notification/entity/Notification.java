package project.backend.domain.notification.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.backend.domain.common.entity.BaseEntity;
import project.backend.domain.like.entity.CulturalEventLike;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String title;

    public String content;

    @OneToOne
    @JoinColumn(name = "cultural_event_like_id")
    private CulturalEventLike culturalEventLike;

    @Builder
    public Notification(String title, String content, CulturalEventLike culturalEventLike) {
        this.title = title;
        this.content = content;
        this.culturalEventLike = culturalEventLike;
    }
}
