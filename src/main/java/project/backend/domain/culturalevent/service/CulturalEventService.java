package project.backend.domain.culturalevent.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.domain.culturalevent.entity.CulturalEvent;
import project.backend.domain.culturalevent.repository.CulturalEventRepository;
import project.backend.domain.like.entity.CulturalEventLike;
import project.backend.domain.like.repository.CulturalEventLikeRepository;
import project.backend.domain.culturalevnetcategory.entity.CategoryTitle;
import project.backend.domain.member.entity.Member;
import project.backend.domain.member.service.MemberJwtService;
import project.backend.domain.notification.service.NotificationService;
import project.backend.domain.visit.entity.CulturalEventVisit;
import project.backend.domain.visit.repository.CulturalEventVisitRepository;
import project.backend.global.error.exception.BusinessException;
import project.backend.global.error.exception.ErrorCode;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class CulturalEventService {
    private final CulturalEventRepository culturalEventRepository;
    private final CulturalEventLikeRepository culturalEventLikeRepository;
    private final MemberJwtService memberJwtService;
    private final CulturalEventVisitRepository culturalEventVisitRepository;
    private final NotificationService notificationService;

    public List<CulturalEvent> getCulturalEventList(int page, int size, List<CategoryTitle> categories, String ordering, Boolean isOpened, Double latitude, Double longitude) {
        return culturalEventRepository.getCulturalEventList(page, size, categories, ordering, isOpened, latitude, longitude);
    }

    public List<CulturalEvent> getCulturalEventSearchList(int page, int size, String keyword) {
        return culturalEventRepository.getCulturalEventSearchList(page, size, keyword);
    }

    public CulturalEvent getCulturalEvent(Long id) {
        return verifiedCulturalEvent(id);
    }

    public void like(Long id) {
        Member member = memberJwtService.getMember();
        CulturalEvent culturalEvent = getCulturalEvent(id);

        if (culturalEvent.findMemberLike(member).isPresent()) {
            return;
        }

        CulturalEventLike culturalEventLike = new CulturalEventLike();
        culturalEventLike.setCulturalEventLike(member, getCulturalEvent(id));
        culturalEventLikeRepository.save(culturalEventLike);

        // Set Ticket Open Notification
        LocalDateTime ticketOpenDate = culturalEventLike.getCulturalEvent().getTicketOpenDate();
        LocalDateTime currentTimePlus30Minutes = LocalDateTime.now().plusMinutes(30);

        if (ticketOpenDate.isAfter(currentTimePlus30Minutes)) {
            notificationService.createCulturalEventLikeNotification(culturalEventLike);
        }
    }

    public void unLike(Long id) {

        Member member = memberJwtService.getMember();
        CulturalEvent culturalEvent = getCulturalEvent(id);
        Optional<CulturalEventLike> culturalEventLikeOptional = culturalEvent.findMemberLike(member);

        if (culturalEventLikeOptional.isEmpty()) {
            return;
        }

        CulturalEventLike culturalEventLike = culturalEventLikeOptional.get();
        notificationService.deleteCulturalEventLikeNotification(culturalEventLike);
        culturalEventLikeRepository.delete(culturalEventLike);
    }

    public void visit(CulturalEvent culturalEvent) {

        Member member = memberJwtService.getMember();
        if (member == null) {
            return;
        }

        // Variables
        LocalDateTime now = LocalDateTime.now();
        Optional<CulturalEventVisit> optionalCulturalEventVisit = culturalEvent.findMemberVisit(member);

        // 3시간 이내에 방문한 적 있다면 집계 x
        if (optionalCulturalEventVisit.isPresent() && Duration.between(optionalCulturalEventVisit.get().getCreatedDate(), now).toHours() <= 3) {
            return;
        }

        // CulturalEventVisit 생성
        CulturalEventVisit culturalEventVisit = new CulturalEventVisit();
        culturalEventVisit.setCulturalEventVisit(member, culturalEvent);
        culturalEventVisitRepository.save(culturalEventVisit);
    }

    private CulturalEvent verifiedCulturalEvent(Long id) {
        return culturalEventRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.CULTURAL_EVENT));
    }
}
