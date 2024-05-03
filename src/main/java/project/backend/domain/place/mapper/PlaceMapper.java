package project.backend.domain.place.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import project.backend.domain.place.dto.PlaceCreateDto;
import project.backend.domain.place.dto.PlaceRetrieveDto;
import project.backend.domain.place.entity.Place;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PlaceMapper {
    Place placeCreateDtoToPlace(PlaceCreateDto placeCreateDto);

    PlaceRetrieveDto placeToPlaceResponseDto(Place place);

}
