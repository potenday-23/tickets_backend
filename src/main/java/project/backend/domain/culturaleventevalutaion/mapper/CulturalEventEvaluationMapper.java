package project.backend.domain.culturaleventevalutaion.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import project.backend.domain.culturalevent.dto.CulturalEventCreateDto;
import project.backend.domain.culturalevent.dto.CulturalEventListDto;
import project.backend.domain.culturalevent.dto.CulturalEventRetrieveDto;
import project.backend.domain.culturalevent.entity.CulturalEvent;
import project.backend.domain.culturaleventevalutaion.dto.CulturalEventEvaluationListDto;
import project.backend.domain.culturaleventevalutaion.entity.CulturalEventEvaluation;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CulturalEventEvaluationMapper {

    @Mapping(source = "culturalEvent.id", target = "culturalEventId")
    @Mapping(source = "culturalEvent.title", target = "culturalEventTitle")
    CulturalEventEvaluationListDto culturalEventEvaluationToCulturalEventEvaluationListDto(CulturalEventEvaluation CulturalEventEvaluation);

    List<CulturalEventEvaluationListDto> culturalEventEvaluationToCulturalEventEvaluationListDtos(List<CulturalEventEvaluation> CulturalEventEvaluationList);
}
