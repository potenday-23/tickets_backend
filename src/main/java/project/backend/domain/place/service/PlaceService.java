package project.backend.domain.place.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import project.backend.domain.place.dto.PlaceCreateDto;
import project.backend.domain.place.entity.Place;
import project.backend.domain.place.mapper.PlaceMapper;
import project.backend.domain.place.repository.PlaceRepository;
import project.backend.domain.ticket.entity.Ticket;
import project.backend.global.error.exception.BusinessException;
import project.backend.global.error.exception.ErrorCode;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final PlaceMapper placeMapper;

    /**
     * Place 생성
     *
     * @param placeCreateDto
     * @return Place
     */
    public Place createPlace(PlaceCreateDto placeCreateDto) {
        Place place = placeRepository
                .findFirstByAddress(placeCreateDto.getAddress())
                .orElseGet(() -> placeMapper.placeCreateDtoToPlace(placeCreateDto));
        placeRepository.save(place);
        return place;
    }

    public PlaceCreateDto getPlaceCreateDtoFromPlaceCode(String placeCode) {

        // URL 조회
        RestTemplate restTemplate = new RestTemplate();
        String placeUrl = "https://api-ticketfront.interpark.com/v1/Place/" + placeCode;
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                placeUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {
                });
        Map<String, Object> responseBody = response.getBody();

        // data 파싱
        Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(data, PlaceCreateDto.class);
    }

}
