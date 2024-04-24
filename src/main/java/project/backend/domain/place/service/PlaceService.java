package project.backend.domain.place.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.domain.place.dto.PlacePostRequestDto;
import project.backend.domain.place.entity.Place;
import project.backend.domain.place.mapper.PlaceMapper;
import project.backend.domain.place.repository.PlaceRepository;
import project.backend.global.error.exception.BusinessException;
import project.backend.global.error.exception.ErrorCode;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final PlaceMapper placeMapper;

    public Place createPlace(PlacePostRequestDto placePostRequestDto) {
        Place place = Place.builder()
                .title(placePostRequestDto.getTitle())
                .address(placePostRequestDto.getAddress())
                .latitude(placePostRequestDto.getLatitude())
                .longitude(placePostRequestDto.getLongitude()).build();
        placeRepository.save(place);
        return place;
    }

    public Place getPlace(Long id) {
        return verifiedPlace(id);
    }

    public List<Place> getPlaceList() {
        return placeRepository.findAll();
    }
    public void deletePlace(Long id) {
        placeRepository.delete(verifiedPlace(id));
    }

    private Place verifiedPlace(Long id) {
        return placeRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.NOTICE_NOT_FOUND));
    }

}
