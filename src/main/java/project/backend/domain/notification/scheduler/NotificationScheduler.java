package project.backend.domain.notification.scheduler;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import project.backend.domain.member.entity.Member;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Component
public class NotificationScheduler {

    private final ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();


    public NotificationScheduler() {
        taskScheduler.initialize();
    }

    public void scheduleNotification(Long notificationId, Date triggerDate, Member member, String title, String body) {
        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(() -> {
            sendNotification(member, title, body);
        }, triggerDate);
        scheduledTasks.put(notificationId, scheduledTask);
    }

    public void cancelNotification(Long notificationId) {
        ScheduledFuture<?> scheduledTask = scheduledTasks.remove(notificationId);
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
        }
    }

    public void sendNotification(Member member, String title, String body) {
        String fcmToken = member.getFcmToken();

        if (fcmToken != null) {
            com.google.firebase.messaging.Notification notification = com.google.firebase.messaging.Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();

            Message message = Message.builder()
                    .setToken(fcmToken)
                    .setNotification(notification)
                    .build();
            try {
                String response = FirebaseMessaging.getInstance().send(message);
                System.out.println("Successfully sent message: " + response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}