package project.backend.domain.place.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import project.backend.domain.place.dto.PlacePostRequestDto;
import project.backend.domain.place.dto.PlaceResponseDto;
import project.backend.domain.place.entity.Place;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PlaceMapper {
    Place placePostRequestDtoToPlace(PlacePostRequestDto placePostRequestDto);

    PlaceResponseDto placeToPlaceResponseDto(Place place);

}
