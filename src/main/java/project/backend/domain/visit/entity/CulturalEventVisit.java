package project.backend.domain.visit.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import project.backend.domain.common.entity.BaseEntity;
import project.backend.domain.culturalevent.entity.CulturalEvent;
import project.backend.domain.member.entity.Member;
import project.backend.domain.visit.CulturalEventVisitListener;

import javax.persistence.*;
import java.util.Optional;

@Entity
@EntityListeners(CulturalEventVisitListener.class)
@Getter
@NoArgsConstructor
public class CulturalEventVisit extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    public Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    public CulturalEvent culturalEvent;

    // == 연관관계 매핑 == //
    public void setCulturalEventVisit(Member member, CulturalEvent culturalEvent) {
        if (this.member != null) {
            if (this.member.getCulturalEventVisitList().contains(this)) {
                this.member.getCulturalEventVisitList().remove(this);
            }
        }
        if (this.culturalEvent != null) {
            if (this.culturalEvent.getCulturalEventVisitList().contains(this)) {
                this.culturalEvent.getCulturalEventVisitList().remove(this);
            }
        }
        this.member = Optional.ofNullable(member).orElse(this.member);
        this.member.getCulturalEventVisitList().add(this);
        this.culturalEvent = Optional.ofNullable(culturalEvent).orElse(this.culturalEvent);
        this.culturalEvent.getCulturalEventVisitList().add(this);
    }
}
