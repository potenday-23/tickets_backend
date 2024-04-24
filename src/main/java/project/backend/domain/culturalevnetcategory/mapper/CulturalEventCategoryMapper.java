package project.backend.domain.culturalevnetcategory.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import project.backend.domain.culturalevnetcategory.dto.CulturalEventCategoryPostRequestDto;
import project.backend.domain.culturalevnetcategory.dto.CulturalEventCategoryResponseDto;
import project.backend.domain.culturalevnetcategory.entity.CulturalEventCategory;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CulturalEventCategoryMapper {
    CulturalEventCategory culturalEventCategoryPostRequestDtoToCulturalEventCategory(CulturalEventCategoryPostRequestDto culturalEventCategoryPostRequestDto);

    CulturalEventCategoryResponseDto culturalEventCategoryToCulturalEventCategoryResponseDto(CulturalEventCategory culturalEventCategory);

}
