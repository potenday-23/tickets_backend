package project.backend.domain.culturalevent.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import project.backend.domain.culturalevent.dto.CulturalEventCreateDto;
import project.backend.domain.culturalevent.dto.CulturalEventListDto;
import project.backend.domain.culturalevent.dto.CulturalEventResponseDto;
import project.backend.domain.culturalevent.entity.CulturalEvent;
import project.backend.domain.culturalevent.entity.CulturalEventStatus;
import project.backend.domain.culturalevnetcategory.mapper.CulturalEventCategoryMapper;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CulturalEventMapper {
    CulturalEventMapper INSTANCE = Mappers.getMapper(CulturalEventMapper.class);

    CulturalEvent culturalEventCreateDtoToCulturalEvent(CulturalEventCreateDto culturalEventCreateDto);

    @Mapping(source = "culturalEventCategory.title.name", target = "categoryName")
    @Mapping(source = "place.name", target = "placeName")
    @Mapping(target = "status", expression = "java(calculateStatus(culturalEvent.getStartDate(), culturalEvent.getEndDate()))")
    CulturalEventListDto culturalEventToCulturalEventListDto(CulturalEvent culturalEvent);

    List<CulturalEventListDto> culturalEventToCulturalEventListDtos(List<CulturalEvent> culturalEventList);

    default String calculateStatus(Date startDate, Date endDate) {
        Date now = new Date();

        if (now.before(startDate)) {
            return CulturalEventStatus.UPCOMING.getStatus();
        } else if (now.after(endDate)) {
            return CulturalEventStatus.CLOSED.getStatus();
        } else {
            return CulturalEventStatus.OPENING.getStatus();
        }
    }

}
