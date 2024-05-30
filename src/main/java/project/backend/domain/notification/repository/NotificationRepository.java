package project.backend.domain.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.backend.domain.like.entity.CulturalEventLike;
import project.backend.domain.notification.entity.Notification;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<Notification> findNotificationByCulturalEventLikeId(Long culturalEventLikeId);
}
