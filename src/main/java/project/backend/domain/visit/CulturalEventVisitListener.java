package project.backend.domain.visit;

import project.backend.domain.visit.entity.CulturalEventVisit;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;

public class CulturalEventVisitListener {
    @PrePersist
    public void postPersist(CulturalEventVisit CulturalEventVisit) {
        CulturalEventVisit.culturalEvent.increaseVisitCount();
    }
}
