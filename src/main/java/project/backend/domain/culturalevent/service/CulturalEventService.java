package project.backend.domain.culturalevent.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.domain.culturalevent.dto.CulturalEventPostRequestDto;
import project.backend.domain.culturalevent.entity.CulturalEvent;
import project.backend.domain.culturalevent.repository.CulturalEventRepository;
import project.backend.domain.culturalevnetcategory.entity.CategoryTitle;
import project.backend.domain.culturalevnetcategory.entity.CulturalEventCategory;
import project.backend.domain.culturalevnetcategory.service.CulturalEventCategoryService;
import project.backend.global.error.exception.BusinessException;
import project.backend.global.error.exception.ErrorCode;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CulturalEventService {
    private final CulturalEventRepository culturalEventRepository;
    private final CulturalEventCategoryService culturalEventCategoryService;

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
        CulturalEvent culturalEvent = getCulturalEvent(id);

    }

    public CulturalEvent createCulturalEvent(CulturalEventPostRequestDto culturalEventPostRequestDto) {
        CulturalEvent culturalEvent = CulturalEvent.builder()
                .title(culturalEventPostRequestDto.getTitle())
                .thumbnailImageUrl(culturalEventPostRequestDto.getThumbnailImageUrl())
                .startDate(culturalEventPostRequestDto.getStartDate())
                .endDate(culturalEventPostRequestDto.getEndDate())
                .ticketOpenDate(culturalEventPostRequestDto.getTicketOpenDate())
                .runningTime(culturalEventPostRequestDto.getRunningTime())
                .summary(culturalEventPostRequestDto.getSummary())
                .genre(culturalEventPostRequestDto.getGenre())
                .information(culturalEventPostRequestDto.getInformation()).build();
        culturalEventRepository.save(culturalEvent);




        return culturalEvent;
    }


    public void deleteCulturalEvent(Long id) {
        culturalEventRepository.delete(verifiedCulturalEvent(id));
    }

    private CulturalEvent verifiedCulturalEvent(Long id) {
        return culturalEventRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.CULTURAL_EVENT));
    }

}
