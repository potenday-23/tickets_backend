package project.backend.domain.member.service;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import project.backend.domain.member.entity.Member;
import project.backend.domain.member.repository.MemberRepository;
import project.backend.global.error.exception.BusinessException;
import project.backend.global.error.exception.ErrorCode;

import java.util.Optional;

@Configuration
@AllArgsConstructor
public class MemberJwtService {
    private final MemberRepository memberRepository;

    public Member getMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof String && "anonymousUser".equals(principal)) {
            return null;
        }

        Long memberId = null;
        if (principal instanceof Long) {
            memberId = (Long) principal;
        }

        if (memberId == null) {
            return null;
        }

        Optional<Member> member = memberRepository.findById(memberId);
        return member.orElseThrow(() -> new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}