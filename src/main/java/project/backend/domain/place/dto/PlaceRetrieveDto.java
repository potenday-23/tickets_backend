package project.backend.domain.place.dto;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceRetrieveDto {
    private String title;
    private String address;
    private Double latitude;
    private Double longitude;
}