package project.backend.domain.suspend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.domain.member.entity.Member;
import project.backend.domain.member.service.MemberService;
import project.backend.domain.suspend.dto.SuspendPatchRequestDto;
import project.backend.domain.suspend.dto.SuspendPostRequestDto;
import project.backend.domain.suspend.entity.Suspend;
import project.backend.domain.suspend.mapper.SuspendMapper;
import project.backend.domain.suspend.repository.SuspendRepository;
import project.backend.global.error.exception.BusinessException;
import project.backend.global.error.exception.ErrorCode;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SuspendService {
    private final SuspendRepository suspendRepository;
    private final SuspendMapper suspendMapper;
    private final MemberService memberService;

    public Suspend createSuspend(int suspendPeriod, String suspendReason, Long memberId){
        Suspend suspend = Suspend.builder()
                .suspendDeadline(LocalDate.now().plusDays(suspendPeriod))
                .suspendReason(suspendReason)
                .member(memberService.verifiedMember(memberId)).build();
        suspendRepository.save(suspend);
        return suspend;
    }

    public Suspend getSuspend(Long id) {
        return verifiedSuspend(id);
    }

    public List<Suspend> getSuspendList() {
        return suspendRepository.findAll();
    }

    public Suspend editSuspend(Long id, int suspendPeriod, String suspendReason) {
        Suspend suspend = verifiedSuspend(id).patchSuspend(LocalDate.now().plusDays(suspendPeriod), suspendReason);
        suspendRepository.save(suspend);
        return suspend;
    }

    public void deleteSuspend(Long id) {
        suspendRepository.delete(verifiedSuspend(id));
    }

    private Suspend verifiedSuspend(Long id) {
        return suspendRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.NOTICE_NOT_FOUND));
    }

}
