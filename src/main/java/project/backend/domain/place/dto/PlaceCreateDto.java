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
    private String latitude;

    @JsonProperty("longitude")
    private String longitude;
}