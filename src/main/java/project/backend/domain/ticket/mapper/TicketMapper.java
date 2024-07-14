package project.backend.domain.ticket.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import project.backend.domain.ticket.dto.TicketRetrieveDto;
import project.backend.domain.ticket.entity.Ticket;

import java.util.List;

// todo: ReportingPolicy.IGNORE은 무슨 뜻일까?
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketMapper {

    TicketRetrieveDto ticketToTicketRetrieveDto(Ticket ticket);

}
