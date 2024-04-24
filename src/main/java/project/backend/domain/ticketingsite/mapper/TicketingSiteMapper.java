package project.backend.domain.ticketingsite.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import project.backend.domain.ticketingsite.dto.TicketingSitePostRequestDto;
import project.backend.domain.ticketingsite.dto.TicketingSiteResponseDto;
import project.backend.domain.ticketingsite.entity.TicketingSite;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketingSiteMapper {
    TicketingSite ticketingSitePostRequestDtoToTicketingSite(TicketingSitePostRequestDto ticketingSitePostRequestDto);

    TicketingSiteResponseDto ticketingSiteToTicketingSiteResponseDto(TicketingSite ticketingSite);

}
