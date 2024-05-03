package project.backend.domain.culturalevent.mapper;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import project.backend.domain.culturalevent.dto.CulturalEventCreateDto;
import project.backend.domain.culturalevent.dto.CulturalEventListDto;
import project.backend.domain.culturalevent.dto.CulturalEventRetrieveDto;
import project.backend.domain.culturalevent.entity.CulturalEvent;
import project.backend.domain.culturalevnetinfo.entity.CulturalEventInfo;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CulturalEventMapper {



    CulturalEventMapper INSTANCE = Mappers.getMapper(CulturalEventMapper.class);

    @Mapping(source = "culturalEventCategory.title.name", target = "categoryName")
    CulturalEventRetrieveDto culturalEventToCulturalEventRetrieveDto(CulturalEvent culturalEvent);

    CulturalEvent culturalEventCreateDtoToCulturalEvent(CulturalEventCreateDto culturalEventCreateDto);

    @Mapping(source = "culturalEventCategory.title.name", target = "categoryName")
    @Mapping(source = "place.name", target = "placeName")
    @Mapping(target = "isOpened", expression = "java(calculateIsOpened(culturalEvent.getTicketOpenDate()))")
    CulturalEventListDto culturalEventToCulturalEventListDto(CulturalEvent culturalEvent);

    List<CulturalEventListDto> culturalEventToCulturalEventListDtos(List<CulturalEvent> culturalEventList);

    default Boolean calculateIsOpened(LocalDateTime ticketOpenDate) {
        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(ticketOpenDate)) {
            return false;
        } else {
            return true;
        }
    }
}
