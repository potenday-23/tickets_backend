package project.backend.domain.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.domain.like.entity.CulturalEventLike;
import project.backend.domain.notification.entity.Notification;
import project.backend.domain.notification.repository.NotificationRepository;
import project.backend.domain.notification.scheduler.NotificationScheduler;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {
    private final NotificationScheduler notificationScheduler;
    private final NotificationRepository notificationRepository;


    public void createCulturalEventLikeNotification(CulturalEventLike culturalEventLike) {
        Notification notification = Notification.builder()
                .title("알림 타이틀")
                .content("알림 콘텐츠")
                .culturalEventLike(culturalEventLike)
                .build();
        notificationRepository.save(notification);

        // 티켓 오픈 30분 전
        ZonedDateTime ticketOpenDateTime = culturalEventLike.culturalEvent.ticketOpenDate.atZone(ZoneId.systemDefault());
        ZonedDateTime scheduledDateTime = ticketOpenDateTime.minusMinutes(30);
        Date scheduledTime = Date.from(scheduledDateTime.toInstant());

        // Notification 설정
        notificationScheduler.scheduleNotification(
                notification.getId(),
                scheduledTime,
                culturalEventLike.getMember(),
                notification.getTitle(),
                notification.getContent()
        );
    }

    public void deleteCulturalEventLikeNotification(CulturalEventLike culturalEventLike) {
        Optional<Notification> notificationOptional = notificationRepository.findNotificationByCulturalEventLikeId(culturalEventLike.id);

        if (notificationOptional.isPresent()) {
            Notification notification = notificationOptional.get();
            notificationScheduler.cancelNotification(notification.id);
        }
    }
}
