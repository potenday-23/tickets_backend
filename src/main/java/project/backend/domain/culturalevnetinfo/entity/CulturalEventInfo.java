package project.backend.domain.culturalevnetinfo.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.backend.domain.common.entity.BaseEntity;
import project.backend.domain.culturalevent.entity.CulturalEvent;
import project.backend.domain.member.entity.Member;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CulturalEventInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String imageUrl;

    public String text;

    public Integer ordering;

    @ManyToOne(fetch = FetchType.LAZY)
    public CulturalEvent culturalEvent;

    @Builder
    public CulturalEventInfo(String imageUrl, String text, Integer ordering) {
        this.imageUrl = imageUrl;
        this.text = text;
        this.ordering = ordering;
    }

    // == 연관관계 매핑 == //
    public void setCulturalEvent(CulturalEvent culturalEvent) {
        if (this.culturalEvent != null) {
            if (this.culturalEvent.getCulturalEvnetInfoList().contains(this)) {
                this.culturalEvent.getCulturalEvnetInfoList().remove(this);
            }
        }
        this.culturalEvent = Optional.ofNullable(culturalEvent).orElse(this.culturalEvent);
        this.culturalEvent.getCulturalEvnetInfoList().add(this);
    }
}
