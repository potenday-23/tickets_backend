package project.backend.domain.culturalevent.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
import project.backend.domain.culturaleventevalutaion.service.CulturalEventEvaluationService;
import project.backend.domain.culturalevnetcategory.entity.CategoryTitle;
import project.backend.domain.culturalevnetcategory.entity.CulturalEventCategory;
import project.backend.domain.culturalevnetcategory.service.CulturalEventCategoryService;
import project.backend.domain.culturalevnetinfo.dto.CulturalEventInfoCreateDto;
import project.backend.domain.culturalevnetinfo.entity.CulturalEventInfo;
import project.backend.domain.culturalevnetinfo.service.CulturalEventInfoService;
import project.backend.domain.place.dto.PlaceCreateDto;
import project.backend.domain.place.entity.Place;
import project.backend.domain.place.service.PlaceService;
import project.backend.domain.ticketingsite.entity.TicketingSite;
import project.backend.domain.ticketingsite.service.TicketingSiteService;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class CulturalEventBatchService {

    private final CulturalEventRepository culturalEventRepository;
    private final CulturalEventMapper culturalEventMapper;
    private final PlaceService placeService;
    private final CulturalEventCategoryService culturalEventCategoryService;
    private final CulturalEventInfoService culturalEventInfoService;
    private final TicketingSiteService ticketingSiteService;
    private final CulturalEventEvaluationService culturalEventEvaluationService;

    /**
     * CulturalEvent 생성
     */
    public void createCulturalEvents() {
        Map<CategoryTitle, List<String>> interparkGoodsCodeMap = getInterparkGoodsCodeMap();
        for (CategoryTitle categoryTitle : interparkGoodsCodeMap.keySet()) {
            for (String goodsCode : interparkGoodsCodeMap.get(categoryTitle)) {
                CulturalEventCreateDto culturalEventCreateDto = getDetailCulturalEventFromGoodsCode(goodsCode, categoryTitle);
                createCulturalEvent(goodsCode, culturalEventCreateDto);
            }

        }
    }

    /**
     * CulturalEvent 생성
     *
     * @param culturalEventCreateDto
     * @return Place
     */
    public CulturalEvent createCulturalEvent(String goodsCode, CulturalEventCreateDto culturalEventCreateDto) {
        CulturalEvent culturalEvent;

        // CulturalEvent 중복 확인 및 생헝
        Optional<CulturalEvent> optionalCulturalEvent = culturalEventRepository.findFirstByTitle(culturalEventCreateDto.getTitle());
        if (optionalCulturalEvent.isPresent()) {
            culturalEvent = optionalCulturalEvent.get();
            return culturalEvent;
        } else {
            culturalEvent = culturalEventMapper.culturalEventCreateDtoToCulturalEvent(culturalEventCreateDto);
        }
        // Place 연관관계 매핑
        PlaceCreateDto placeCreateDto = placeService.getPlaceCreateDtoFromPlaceCode(culturalEventCreateDto.getPlaceCode());
        Place place = placeService.createPlace(placeCreateDto);
        culturalEvent.setPlace(place);

        // CulturalEventCategory 연관관계 매핑
        CulturalEventCategory culturalEventCategory = culturalEventCategoryService.verifiedCulturalEventCategoryByTitle(culturalEventCreateDto.getCategoryTitle());
        culturalEvent.setCulturalEventCategory(culturalEventCategory);

        // TicketingSite 연관관계 매핑
        List<TicketingSite> ticketingSiteList = ticketingSiteService.createTicketingSite(goodsCode, culturalEvent.getTitle());
        ticketingSiteList.stream().forEach(ticketingSite -> {
            ticketingSite.setCulturalEvent(culturalEvent);
        });

        // 저장
        CulturalEvent savedCulturalEvent = culturalEventRepository.save(culturalEvent);

        // CulturalEventEvaluation 연관관계 매핑
        culturalEventEvaluationService.createCulturalEventEvaluations(goodsCode, savedCulturalEvent);

        // CulturalEventInfo 연관관계 매핑
        List<String> imageUrlList = culturalEventInfoService.extractImageUrlList(culturalEventCreateDto.getInformation());
        for (int i = 0; i < imageUrlList.size(); i++) {
            CulturalEventInfoCreateDto culturalEventInfoCreateDto = CulturalEventInfoCreateDto.builder().imageUrl(imageUrlList.get(i)).ordering(i).build();
            CulturalEventInfo culturalEventInfo = culturalEventInfoService.createCulturalEventInfo(culturalEventInfoCreateDto);
            culturalEventInfo.setCulturalEvent(culturalEvent);
        }

        return culturalEvent;
    }


    /**
     * interpark의 최신 goodsCode 가져오기
     *
     * @return List
     */
    public Map<CategoryTitle, List<String>> getInterparkGoodsCodeMap() {
        RestTemplate restTemplate = new RestTemplate();
        String baseUrl = "https://tickets.interpark.com/contents/api/goods/genre";
        Map<CategoryTitle, List<String>> goodsCodeMap = new HashMap<>();

        for (CategoryTitle categoryTitle : CategoryTitle.values()) {
            if (categoryTitle != categoryTitle.ALL) {
                List<String> goodsCodeList = new ArrayList<>();
                UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                        .queryParam("genre", categoryTitle.getType())
                        .queryParam("page", "2")
                        .queryParam("pageSize", "20");

                ResponseEntity<String> response = restTemplate.exchange(
                        uriBuilder.toUriString(),
                        HttpMethod.GET,
                        null,
                        String.class);

                JSONArray jsonArray = new JSONArray(response.getBody());

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    goodsCodeList.add(jsonObject.getString("goodsCode"));
                }
                goodsCodeMap.put(categoryTitle, goodsCodeList);
            }
        }
        return goodsCodeMap;
    }

    /**
     * goodsCode로 세부 정보 찾기
     */
    public CulturalEventCreateDto getDetailCulturalEventFromGoodsCode(String goodsCode, CategoryTitle categoryTitle) {
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
        CulturalEventCreateDto culturalEventCreateDto = mapper.convertValue(data, CulturalEventCreateDto.class);

        // category Title 설정
        culturalEventCreateDto.setCategoryTitle(categoryTitle);
        culturalEventCreateDto.setThumbnailImageUrl("https:" + culturalEventCreateDto.getThumbnailImageUrl());
        return culturalEventCreateDto;
    }
}


