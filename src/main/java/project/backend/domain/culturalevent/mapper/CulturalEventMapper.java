package project.backend.domain.culturalevent.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import project.backend.domain.culturalevent.dto.CulturalEventPostRequestDto;
import project.backend.domain.culturalevent.dto.CulturalEventResponseDto;
import project.backend.domain.culturalevent.entity.CulturalEvent;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CulturalEventMapper {
    CulturalEvent culturalEventPostRequestDtoToCulturalEvent(CulturalEventPostRequestDto culturalEventPostRequestDto);

    CulturalEventResponseDto culturalEventToCulturalEventResponseDto(CulturalEvent culturalEvent);

}
