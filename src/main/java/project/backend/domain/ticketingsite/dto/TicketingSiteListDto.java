package project.backend.domain.ticketingsite.dto;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketingSiteListDto {
    private String platform;
    private String imageUrl;
    private String link;
}