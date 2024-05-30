package project.backend.domain.fcm.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.backend.domain.fcm.service.FcmService;

@RestController
@RequestMapping("/api/fcm")
@RequiredArgsConstructor
@Api(tags = "테스트용 FCM - 삭제 예정")
public class FcmController {

    private final FcmService fcmService;

    @PostMapping("/send")
    public String sendNotification(@RequestParam String token,
                                   @RequestParam String title,
                                   @RequestParam String body){
        fcmService.sendNotificationByToken(token, title, body);
        return "Notification sent successfully";
    }
}
