package project.backend.backoffice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import project.backend.domain.member.entity.Member;
import project.backend.domain.member.service.MemberService;
import project.backend.domain.suspend.dto.SuspendPostRequestDto;
import project.backend.domain.suspend.entity.Suspend;
import project.backend.domain.suspend.repository.SuspendRepository;
import project.backend.domain.suspend.service.SuspendService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Controller
@EnableWebMvc
@RequestMapping("/backoffice/members")
@RequiredArgsConstructor
public class MemberBackofficeController {

    private final MemberService memberService;
    private final SuspendService suspendService;
    private final SuspendRepository suspendRepository;

    @GetMapping
    public String suspendMember(Model model) {
        List<Member> memberList = memberService.getMemberList();
        model.addAttribute("members", memberList);
        return "members/members";
    }

    @GetMapping("/{memberId}/suspend")
    public String suspendMember(@PathVariable("memberId") Long memberId,
                                Model model) {

        // 원래 설정된 값 있으면 불러오기
        SuspendPostRequestDto suspendPostRequestDto;

        Optional<Suspend> suspend = suspendRepository.findFirstByMemberId(memberId);
        if (suspend.isPresent()) {
            int suspendPeriod = Long.valueOf(ChronoUnit.DAYS.between(LocalDate.now(), suspend.get().suspendDeadline)).intValue();
            suspendPostRequestDto = SuspendPostRequestDto.builder().suspendPeriod(suspendPeriod).suspendReason(suspend.get().suspendReason).build();
        } else {
            suspendPostRequestDto = new SuspendPostRequestDto();
        }

        // 초기 값 세팅
        model.addAttribute("form", suspendPostRequestDto);
        model.addAttribute("memberId", memberId);

        return "members/suspend";
    }

    @PostMapping("/{memberId}/suspend")
    public String suspendMember(@PathVariable("memberId") Long memberId,
                                SuspendPostRequestDto suspendPostRequestDto) {
        Optional<Suspend> suspend = suspendRepository.findFirstByMemberId(memberId);

        if (suspend.isPresent()) {
            suspendService.editSuspend(suspend.get().getId(), suspendPostRequestDto.suspendPeriod, suspendPostRequestDto.suspendReason);
        } else {
            suspendService.createSuspend(suspendPostRequestDto.suspendPeriod, suspendPostRequestDto.suspendReason, memberId);
        }
        return "redirect:/backoffice/members";
    }
}