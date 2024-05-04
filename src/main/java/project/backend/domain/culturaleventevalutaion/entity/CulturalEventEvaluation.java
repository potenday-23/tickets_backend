package project.backend.domain.culturaleventevalutaion.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.backend.domain.common.entity.BaseEntity;
import project.backend.domain.culturalevent.entity.CulturalEvent;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CulturalEventEvaluation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String title;

    @Column(columnDefinition = "TEXT")
    public String content;

    public Integer score;

    @Enumerated(value = EnumType.STRING)
    public EvaluationType type;


    @ManyToOne(fetch = FetchType.LAZY)
    public CulturalEvent culturalEvent;

    @Builder
    public CulturalEventEvaluation(String title, String content, Integer score, EvaluationType type, CulturalEvent culturalEvent) {
        this.title = title;
        this.content = content;
        this.score = score;
        this.type = type;
        this.culturalEvent = culturalEvent;
    }

    // == 연관관계 매핑 == //
    public void setCulturalEvent(CulturalEvent culturalEvent) {
        if (this.culturalEvent != null) {
            if (this.culturalEvent.getCulturalEventEvaluationList().contains(this)) {
                this.culturalEvent.getCulturalEventEvaluationList().remove(this);
            }
        }
        this.culturalEvent = Optional.ofNullable(culturalEvent).orElse(this.culturalEvent);
        this.culturalEvent.getCulturalEventEvaluationList().add(this);
    }
}
