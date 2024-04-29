package project.backend.domain.culturalevent.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import project.backend.domain.culturalevent.dto.CulturalEventCreateDto;
import project.backend.domain.culturalevent.dto.CulturalEventListDto;
import project.backend.domain.culturalevent.dto.CulturalEventResponseDto;
import project.backend.domain.culturalevent.entity.CulturalEvent;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CulturalEventMapper {

    CulturalEvent culturalEventCreateDtoToCulturalEvent(CulturalEventCreateDto culturalEventCreateDto);

    CulturalEventResponseDto culturalEventToCulturalEventResponseDto(CulturalEvent culturalEvent);

    List<CulturalEventListDto> culturalEventToCulturalEventListDtos(List<CulturalEvent> culturalEventList);

}
