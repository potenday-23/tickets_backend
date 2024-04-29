package project.backend.domain.culturaleventlike.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.domain.culturalevent.entity.CulturalEvent;
import project.backend.domain.culturaleventlike.dto.CulturalEventLikePatchRequestDto;
import project.backend.domain.culturaleventlike.entity.CulturalEventLike;
import project.backend.domain.culturaleventlike.mapper.CulturalEventLikeMapper;
import project.backend.domain.culturaleventlike.repository.CulturalEventLikeRepository;
import project.backend.domain.member.entity.Member;
import project.backend.domain.memberTicketLike.entity.MemberTicketLike;
import project.backend.domain.ticket.entity.Ticket;
import project.backend.global.error.exception.BusinessException;
import project.backend.global.error.exception.ErrorCode;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CulturalEventLikeService {
    private final CulturalEventLikeRepository culturalEventLikeRepository;

    public CulturalEventLike getCulturalEventLike(Long id) {
        return verifiedCulturalEventLike(id);
    }

    public List<CulturalEventLike> getCulturalEventLikeList() {
        return culturalEventLikeRepository.findAll();
    }

    public void deleteCulturalEventLike(Long id) {
        culturalEventLikeRepository.delete(verifiedCulturalEventLike(id));
    }

    private CulturalEventLike verifiedCulturalEventLike(Long id) {
        return culturalEventLikeRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.NOTICE_NOT_FOUND));
    }

}
