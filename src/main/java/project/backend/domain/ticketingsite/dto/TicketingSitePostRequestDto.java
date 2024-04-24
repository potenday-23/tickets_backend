package project.backend.domain.ticketingsite.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketingSitePostRequestDto {
    private String platform;
    private String link;
}