package project.backend.domain.culturalevent.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.domain.culturalevent.entity.CulturalEvent;
import project.backend.domain.culturalevent.repository.CulturalEventRepository;
import project.backend.domain.culturaleventlike.entity.CulturalEventLike;
import project.backend.domain.culturaleventlike.repository.CulturalEventLikeRepository;
import project.backend.domain.culturalevnetcategory.entity.CategoryTitle;
import project.backend.domain.culturalevnetcategory.entity.CulturalEventCategory;
import project.backend.domain.culturalevnetcategory.service.CulturalEventCategoryService;
import project.backend.domain.member.entity.Member;
import project.backend.domain.member.service.MemberJwtService;
import project.backend.global.error.exception.BusinessException;
import project.backend.global.error.exception.ErrorCode;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CulturalEventService {
    private final CulturalEventRepository culturalEventRepository;
    private final CulturalEventCategoryService culturalEventCategoryService;
    private final CulturalEventLikeRepository culturalEventLikeRepository;
    private final MemberJwtService memberJwtService;

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

    public Optional<CulturalEventLike> findMemberLike(Member member, CulturalEvent culturalEvent) {
        return culturalEvent.getCulturalEventLikeList().stream().filter(culturalEventLike -> culturalEventLike.member == member).findFirst();
    }

    public void like(Long id) {
        Member member = memberJwtService.getMember();
        CulturalEvent culturalEvent = getCulturalEvent(id);

        if (findMemberLike(member, culturalEvent).isPresent()) {
            return;
        }

        CulturalEventLike culturalEventLike = new CulturalEventLike();
        culturalEventLike.setCulturalEventLike(memberJwtService.getMember(), getCulturalEvent(id));
        culturalEventLikeRepository.save(culturalEventLike);
    }

    public void unLike(Long id) {
        Member member = memberJwtService.getMember();
        CulturalEvent culturalEvent = getCulturalEvent(id);
        Optional<CulturalEventLike> culturalEventLike = findMemberLike(member, culturalEvent);

        if (culturalEventLike.isEmpty()) {
            return;
        }
        culturalEventLikeRepository.delete(culturalEventLike.get());
    }

    private CulturalEvent verifiedCulturalEvent(Long id) {
        return culturalEventRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.CULTURAL_EVENT));
    }
}
