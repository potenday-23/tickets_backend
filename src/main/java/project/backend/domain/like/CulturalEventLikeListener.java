package project.backend.domain.like;

import org.springframework.stereotype.Component;
import project.backend.domain.like.entity.CulturalEventLike;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;



@Component
public class CulturalEventLikeListener {

    @PrePersist
    public void prePersist(CulturalEventLike culturalEventLike) {
        culturalEventLike.culturalEvent.increaseLikeCount();
    }

    @PreRemove
    public void preRemove(CulturalEventLike culturalEventLike) {
        culturalEventLike.deleteCulturalEventLike();
        culturalEventLike.culturalEvent.decreaseLikeCount();
    }
}