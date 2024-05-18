package project.backend.domain.culturaleventlike.entity;

import lombok.*;
import project.backend.domain.common.entity.BaseEntity;
import project.backend.domain.culturaleventlike.CulturalEventLikeListener;
import project.backend.domain.culturalevent.entity.CulturalEvent;
import project.backend.domain.member.entity.Member;

import javax.persistence.*;
import java.util.Optional;

@Entity
@EntityListeners(CulturalEventLikeListener.class)
@Getter
@NoArgsConstructor
public class CulturalEventLike extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    public Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    public CulturalEvent culturalEvent;

    // == 연관관계 매핑 == //
    public void setCulturalEventLike(Member member, CulturalEvent culturalEvent) {
        if (this.member != null) {
            if (this.member.getCulturalEventLikeList().contains(this)) {
                this.member.getCulturalEventLikeList().remove(this);
            }
        }
        if (this.culturalEvent != null) {
            if (this.culturalEvent.getCulturalEventLikeList().contains(this)) {
                this.culturalEvent.getCulturalEventLikeList().remove(this);
            }
        }
        this.member = Optional.ofNullable(member).orElse(this.member);
        this.member.getCulturalEventLikeList().add(this);
        this.culturalEvent = Optional.ofNullable(culturalEvent).orElse(this.culturalEvent);
        this.culturalEvent.getCulturalEventLikeList().add(this);
    }

    public void deleteCulturalEventLike() {
        this.member.getCulturalEventLikeList().remove(this);
        this.culturalEvent.getCulturalEventLikeList().remove(this);
    }
}
