package project.backend.domain.ticketfolder.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import project.backend.domain.ticketfolder.dto.TicketFolderRetrieveDto;
import project.backend.domain.ticketfolder.entity.TicketFolder;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketFolderMapper {
    List<TicketFolderRetrieveDto> ticketFolderToTicketFolderRetrieveDto(List<TicketFolder> ticketFolders);
    TicketFolderRetrieveDto ticketFolderToTicketFolderRetrieveDto(TicketFolder ticketFolder);

}
