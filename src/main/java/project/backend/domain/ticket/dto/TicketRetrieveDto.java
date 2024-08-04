package project.backend.domain.ticket.dto;

import lombok.*;
import project.backend.domain.ticketfolder.dto.TicketFolderRetrieveDto;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketRetrieveDto {
    // TODO(sprint4) : place 연결 미구현 상태
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
}
