package project.backend.domain.culturalevnetcategory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.domain.culturalevnetcategory.dto.CulturalEventCategoryPostRequestDto;
import project.backend.domain.culturalevnetcategory.entity.CategoryTitle;
import project.backend.domain.culturalevnetcategory.entity.CulturalEventCategory;
import project.backend.domain.culturalevnetcategory.mapper.CulturalEventCategoryMapper;
import project.backend.domain.culturalevnetcategory.repository.CulturalEventCategoryRepository;
import project.backend.global.error.exception.BusinessException;
import project.backend.global.error.exception.ErrorCode;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CulturalEventCategoryService {
    private final CulturalEventCategoryRepository culturalEventCategoryRepository;
    private final CulturalEventCategoryMapper culturalEventCategoryMapper;

    public CulturalEventCategory createCulturalEventCategory(CulturalEventCategoryPostRequestDto culturalEventCategoryPostRequestDto) {
        CulturalEventCategory culturalEventCategory = CulturalEventCategory.builder()
                .title(culturalEventCategoryPostRequestDto.getTitle())
                .ordering(culturalEventCategoryPostRequestDto.getOrdering())
                .imageUrl(culturalEventCategoryPostRequestDto.getImageUrl()).build();
        culturalEventCategoryRepository.save(culturalEventCategory);
        return culturalEventCategory;
    }

    public CulturalEventCategory getCulturalEventCategory(Long id) {
        return verifiedCulturalEventCategory(id);
    }

    public List<CulturalEventCategory> getCulturalEventCategoryList() {
        return culturalEventCategoryRepository.findAll();
    }
    public CulturalEventCategory verifiedCulturalEventCategoryByTitle(CategoryTitle title) {
        return culturalEventCategoryRepository.findFirstByTitle(title).orElseThrow(() -> new BusinessException(ErrorCode.CULTURAL_EVENT_CATEGORY_NOT_FOUND));
    }
    public void deleteCulturalEventCategory(Long id) {
        culturalEventCategoryRepository.delete(verifiedCulturalEventCategory(id));
    }

    private CulturalEventCategory verifiedCulturalEventCategory(Long id) {
        return culturalEventCategoryRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.CULTURAL_EVENT_CATEGORY_NOT_FOUND));
    }



}
