package project.backend.domain.culturalevent.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import project.backend.domain.culturalevent.dto.CulturalEventCreateDto;
import project.backend.domain.culturalevent.entity.CulturalEvent;
import project.backend.domain.culturalevent.mapper.CulturalEventMapper;
import project.backend.domain.culturalevent.repository.CulturalEventRepository;
import project.backend.domain.place.dto.PlaceCreateDto;
import project.backend.domain.place.entity.Place;
import project.backend.domain.place.service.PlaceService;
import project.backend.domain.ticket.dto.TicketPostRequestDto;
import project.backend.domain.ticket.entity.Ticket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class CulturalEventBatchService {

    private final CulturalEventRepository culturalEventRepository;
    private final CulturalEventMapper culturalEventMapper;
    private final PlaceService placeService;

    public void createCulturalEvents() {
        List<String> interparkGoodsCodeList = getinterparkGoodsCodeList();
        for (String goodsCode : interparkGoodsCodeList) {
            CulturalEventCreateDto culturalEventCreateDto = getDetailCulturalEventFromGoodsCode(goodsCode);
            createCulturalEvent(culturalEventCreateDto);
        }
    }

    /**
     * CulturalEvent 생성
     *
     * @param culturalEventCreateDto
     * @return Place
     */
    public CulturalEvent createCulturalEvent(CulturalEventCreateDto culturalEventCreateDto) {

        // CulturalEvent 중복 확인 및 생헝
        CulturalEvent culturalEvent = culturalEventRepository
                .findFirstByTitle(culturalEventCreateDto.getTitle())
                .orElseGet(() -> culturalEventMapper.culturalEventCreateDtoToCulturalEvent(culturalEventCreateDto));

        // place 연관관계 매핑
        PlaceCreateDto placeCreateDto = placeService.getPlaceCreateDtoFromPlaceCode(culturalEventCreateDto.getPlaceCode());
        Place place = placeService.createPlace(placeCreateDto);
        culturalEvent.setPlace(place);

        // 저장
        culturalEventRepository.save(culturalEvent);

        return culturalEvent;
    }


    /**
     * interpark의 최신 goodsCode 가져오기
     *
     * @return List
     */
    public List<String> getinterparkGoodsCodeList() {
        RestTemplate restTemplate = new RestTemplate();
        String baseUrl = "https://tickets.interpark.com/contents/api/goods/genre";

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("genre", "MUSICAL")
                .queryParam("page", "1")
                .queryParam("pageSize", "1000");

        ResponseEntity<String> response = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                null,
                String.class);

        JSONArray jsonArray = new JSONArray(response.getBody());
        List<String> goodsCodes = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            goodsCodes.add(jsonObject.getString("goodsCode"));
        }
        return goodsCodes;
    }

    /**
     * goodsCode로 세부 정보 찾기
     */
    public CulturalEventCreateDto getDetailCulturalEventFromGoodsCode(String goodsCode) {
        RestTemplate restTemplate = new RestTemplate();
        String culturalEventDetailUrl = "https://api-ticketfront.interpark.com/v1/goods/" + goodsCode + "/summary";


        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                culturalEventDetailUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {
                });
        Map<String, Object> responseBody = response.getBody();

        // data 파싱
        Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(data, CulturalEventCreateDto.class);
    }

}
