package project.backend.domain.place.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

// Main Section
@Getter
@Setter
public class PlaceCreateDto {
    @JsonProperty("placeName")
    private String name;

    @JsonProperty("placeAddress")
    private String address;

    @JsonProperty("latitude")
    private Double latitude;

    @JsonProperty("longitude")
    private Double longitude;
}