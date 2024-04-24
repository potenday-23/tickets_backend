package project.backend.domain.ticketingsite.dto;
import lombok.*;

import javax.persistence.Column;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketingSiteResponseDto {
    private String platform;
    private String link;
}