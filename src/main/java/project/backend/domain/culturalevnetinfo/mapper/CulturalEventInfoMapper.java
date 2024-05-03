package project.backend.domain.culturalevnetinfo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import project.backend.domain.culturalevnetinfo.dto.CulturalEventInfoCreateDto;
import project.backend.domain.culturalevnetinfo.dto.CulturalEventInfoResponseDto;
import project.backend.domain.culturalevnetinfo.entity.CulturalEventInfo;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CulturalEventInfoMapper {
    CulturalEventInfo culturalEventInfoPostRequestDtoToCulturalEventInfo(CulturalEventInfoCreateDto culturalEventInfoCreateDto);

    CulturalEventInfoResponseDto culturalEventInfoToCulturalEventInfoResponseDto(CulturalEventInfo culturalEventInfo);

}
