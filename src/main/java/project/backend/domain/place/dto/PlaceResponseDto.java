package project.backend.domain.place.dto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceResponseDto {
    private String title;
    private String address;
    private String latitude;
    private String longitude;
}