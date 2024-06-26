package project.backend.domain.ticketingsite.dto;

import lombok.*;
import project.backend.domain.ticketingsite.entity.Platform;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketingSitePostRequestDto {
    private Platform platform;
    private String link;
}