package project.backend.backoffice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.bind.annotation.*;

@Controller
@EnableWebMvc
@RequestMapping("/backoffice")
@RequiredArgsConstructor
public class BackofficeController {

    @GetMapping
    public String home() {
        return "home/home";
    }

}