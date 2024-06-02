package project.backend.domain.keyword.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import project.backend.domain.keyword.dto.CulturalEventSearchKeywordListDto;
import project.backend.domain.keyword.entity.CulturalEventSearchKeyword;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CulturalEventSearchKeywordMapper {
    List<CulturalEventSearchKeywordListDto> culturalEventSearchKeywordToCulturalEventSearchKeywordListDto(
            List<CulturalEventSearchKeyword> culturalEventSearchKeywordList
    );

}