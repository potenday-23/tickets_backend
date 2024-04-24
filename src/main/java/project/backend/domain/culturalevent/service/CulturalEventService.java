package project.backend.domain.culturalevent.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.domain.culturalevent.dto.CulturalEventPostRequestDto;
import project.backend.domain.culturalevent.entity.CulturalEvent;
import project.backend.domain.culturalevent.repository.CulturalEventRepository;
import project.backend.global.error.exception.BusinessException;
import project.backend.global.error.exception.ErrorCode;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CulturalEventService {
    private final CulturalEventRepository culturalEventRepository;

    public CulturalEvent createCulturalEvent(CulturalEventPostRequestDto culturalEventPostRequestDto) {
        CulturalEvent culturalEvent = CulturalEvent.builder()
                .title(culturalEventPostRequestDto.getTitle())
                .thumbnailImageUrl(culturalEventPostRequestDto.getThumbnailImageUrl())
                .startDate(culturalEventPostRequestDto.getStartDate())
                .endDate(culturalEventPostRequestDto.getEndDate())
                .ticketOpenDate(culturalEventPostRequestDto.getTicketOpenDate())
                .bookingOpenDate(culturalEventPostRequestDto.getBookingOpenDate())
                .runningTime(culturalEventPostRequestDto.getRunningTime())
                .summary(culturalEventPostRequestDto.getSummary())
                .genre(culturalEventPostRequestDto.getGenre())
                .information(culturalEventPostRequestDto.getInformation()).build();
        culturalEventRepository.save(culturalEvent);
        return culturalEvent;
    }

    public CulturalEvent getCulturalEvent(Long id) {
        return verifiedCulturalEvent(id);
    }

    public List<CulturalEvent> getCulturalEventList() {
        return culturalEventRepository.findAll();
    }

    public void deleteCulturalEvent(Long id) {
        culturalEventRepository.delete(verifiedCulturalEvent(id));
    }

    private CulturalEvent verifiedCulturalEvent(Long id) {
        return culturalEventRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.NOTICE_NOT_FOUND));
    }

}
