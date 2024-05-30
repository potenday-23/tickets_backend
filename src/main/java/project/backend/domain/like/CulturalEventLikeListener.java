package project.backend.domain.like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import project.backend.domain.like.entity.CulturalEventLike;
import project.backend.domain.notification.service.NotificationService;

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