package project.backend.domain.like;

import project.backend.domain.like.entity.CulturalEventLike;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;

public class CulturalEventLikeListener {
    @PrePersist
    public void postPersist(CulturalEventLike culturalEventLike) {
        culturalEventLike.culturalEvent.increaseLikeCount();
    }
    @PreRemove
    public void postRemove(CulturalEventLike culturalEventLike) {
        culturalEventLike.deleteCulturalEventLike();
        culturalEventLike.culturalEvent.decreaseLikeCount();
    }
}
