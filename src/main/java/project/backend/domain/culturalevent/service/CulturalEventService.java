package project.backend.domain.culturalevent.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.domain.culturalevent.entity.CulturalEvent;
import project.backend.domain.culturalevent.repository.CulturalEventRepository;
import project.backend.domain.like.entity.CulturalEventLike;
import project.backend.domain.like.repository.CulturalEventLikeRepository;
import project.backend.domain.culturalevnetcategory.entity.CategoryTitle;
import project.backend.domain.culturalevnetcategory.entity.CulturalEventCategory;
import project.backend.domain.culturalevnetcategory.service.CulturalEventCategoryService;
import project.backend.domain.member.entity.Member;
import project.backend.domain.member.service.MemberJwtService;
import project.backend.domain.visit.entity.CulturalEventVisit;
import project.backend.domain.visit.repository.CulturalEventVisitRepository;
import project.backend.global.error.exception.BusinessException;
import project.backend.global.error.exception.ErrorCode;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractAuditable_.createdDate;

@Service
@RequiredArgsConstructor
@Transactional
public class CulturalEventService {
    private final CulturalEventRepository culturalEventRepository;
    private final CulturalEventCategoryService culturalEventCategoryService;
    private final CulturalEventLikeRepository culturalEventLikeRepository;
    private final MemberJwtService memberJwtService;
    private final CulturalEventVisitRepository culturalEventVisitRepository;

    public List<CulturalEvent> getCulturalEventList(CategoryTitle type, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (type == CategoryTitle.ALL || type == null) {
            return culturalEventRepository.findAll(pageable).getContent();
        } else {
            CulturalEventCategory culturalEventCategory = culturalEventCategoryService.verifiedCulturalEventCategoryByTitle(type);
            return culturalEventRepository.findAllByCulturalEventCategory(pageable, culturalEventCategory).getContent();
        }
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
    }

    public void unLike(Long id) {
        Member member = memberJwtService.getMember();
        CulturalEvent culturalEvent = getCulturalEvent(id);
        Optional<CulturalEventLike> culturalEventLike = culturalEvent.findMemberLike(member);

        if (culturalEventLike.isEmpty()) {
            return;
        }
        culturalEventLikeRepository.delete(culturalEventLike.get());
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
