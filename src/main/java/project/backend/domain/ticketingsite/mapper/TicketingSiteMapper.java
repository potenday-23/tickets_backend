package project.backend.domain.ticketingsite.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import project.backend.domain.ticketingsite.dto.TicketingSiteListDto;
import project.backend.domain.ticketingsite.dto.TicketingSitePostRequestDto;
import project.backend.domain.ticketingsite.dto.TicketingSiteResponseDto;
import project.backend.domain.ticketingsite.entity.TicketingSite;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketingSiteMapper {
    TicketingSite ticketingSitePostRequestDtoToTicketingSite(TicketingSitePostRequestDto ticketingSitePostRequestDto);

    TicketingSiteResponseDto ticketingSiteToTicketingSiteResponseDto(TicketingSite ticketingSite);

    @Mapping(source = "platform.name", target = "platform")
    @Mapping(source = "platform.imageUrl", target = "imageUrl")
    TicketingSiteListDto ticketingSiteToTicketingSiteListDto(TicketingSite ticketingSite);

    List<TicketingSiteListDto> ticketingSiteListToTicketingSiteListDtos(List<TicketingSite> ticketingSiteList);


}
