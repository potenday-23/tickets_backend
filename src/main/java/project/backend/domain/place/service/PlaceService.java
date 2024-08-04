package project.backend.domain.place.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import project.backend.domain.place.dto.CrawlPlaceCreateDto;
import project.backend.domain.place.dto.PlaceCreateDto;
import project.backend.domain.place.dto.PlaceRetrieveDto;
import project.backend.domain.place.entity.Place;
import project.backend.domain.place.mapper.PlaceMapper;
import project.backend.domain.place.repository.PlaceRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PlaceService {

    @Value("${jwt.kakao.client_id}")
    private String kakaoClientId;

    private final PlaceRepository placeRepository;
    private final PlaceMapper placeMapper;

    /**
     * Place 생성
     *
     * @param crawlPlaceCreateDto
     * @return Place
     */
    public Place createCrawlPlace(CrawlPlaceCreateDto crawlPlaceCreateDto) {
        if (crawlPlaceCreateDto != null) {
            Place place = placeRepository
                    .findFirstByAddress(crawlPlaceCreateDto.getAddress())
                    .orElseGet(() -> placeMapper.crawlPlaceCreateDtoToPlace(crawlPlaceCreateDto));
            placeRepository.save(place);
            return place;
        } else {
            return null;
        }
    }

    public Place createPlace(PlaceCreateDto placeCreateDto) {
        if (placeCreateDto != null) {
            Place place = placeRepository
                    .findFirstByAddress(placeCreateDto.getAddress())
                    .orElseGet(() -> placeMapper.PlaceCreateDtoToPlace(placeCreateDto));
            placeRepository.save(place);
            return place;
        } else {
            return null;
        }
    }

    public CrawlPlaceCreateDto getCrawlPlaceCreateDtoFromPlaceCode(String placeCode) {

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
        return mapper.convertValue(data, CrawlPlaceCreateDto.class);
    }

    public List<PlaceRetrieveDto> getKakaoPlaces(String search, Integer page, Integer size) {
        List<PlaceRetrieveDto> places = new ArrayList<>();
        try {
            String urlString = String.format("https://dapi.kakao.com/v2/local/search/keyword.json?query=%s&page=%d&size=%d",
                    URLEncoder.encode(search, "UTF-8"), page, size);
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "KakaoAK " + kakaoClientId);

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse JSON response
                JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
                JsonArray documents = jsonObject.getAsJsonArray("documents");

                for (int i = 0; i < documents.size(); i++) {
                    JsonObject document = documents.get(i).getAsJsonObject();
                    PlaceRetrieveDto place = PlaceRetrieveDto.builder()
                            .name(document.get("place_name").getAsString())
                            .address(document.get("road_address_name").getAsString())
                            .latitude(document.get("y").getAsDouble())
                            .longitude(document.get("x").getAsDouble())
                            .build();
                    places.add(place);
                }
            } else {
                System.out.println("GET request not worked");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return places;
    }
}
