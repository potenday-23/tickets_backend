package project.backend.domain.ticket.dto;

import lombok.*;
import project.backend.domain.place.dto.PlaceRetrieveDto;
import project.backend.domain.ticketfolder.dto.TicketFolderRetrieveDto;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketRetrieveDto {
    public Long id;
    public String title;
    public String mainImageUrl;
    public LocalDateTime date;
    public Float score;
    public String seat;
    public Integer price;
    private String review;
    public String mediasData;
    public TicketFolderRetrieveDto ticketFolder;
    public PlaceRetrieveDto place;
}
