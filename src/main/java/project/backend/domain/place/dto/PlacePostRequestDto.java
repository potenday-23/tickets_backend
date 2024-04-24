package project.backend.domain.place.dto;

import lombok.*;

import javax.persistence.Column;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlacePostRequestDto {
    private String title;
    private String address;
    private String latitude;
    private String longitude;
}