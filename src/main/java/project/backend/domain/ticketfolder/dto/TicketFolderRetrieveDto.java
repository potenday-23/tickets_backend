package project.backend.domain.ticketfolder.dto;

import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketFolderRetrieveDto {
    public Long id;
    public String title;
}