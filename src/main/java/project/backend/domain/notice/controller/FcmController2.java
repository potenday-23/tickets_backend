package project.backend.domain.notice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class FcmController2 {

    @GetMapping("/fcm-test")
    public String fcm_test() {
        return "index";
    }
}