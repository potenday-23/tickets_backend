package project.backend.domain.culturaleventcategory.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import project.backend.domain.culturaleventcategory.dto.CulturalEventCategoryListDto;
import project.backend.domain.culturaleventcategory.entity.CulturalEventCategory;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CulturalEventCategoryMapper {
    CulturalEventCategoryMapper INSTANCE = Mappers.getMapper(CulturalEventCategoryMapper.class);

    @Mapping(source = "title.type", target = "type")
    @Mapping(source = "title.name", target = "name")
    CulturalEventCategoryListDto culturalEventCategoryToCulturalEventCategoryResponseDto(CulturalEventCategory culturalEventCategory);

    List<CulturalEventCategoryListDto> culturalEventCategoryToCulturalEventCategoryResponseDtos(List<CulturalEventCategory> culturalEventCategoryList);

}
