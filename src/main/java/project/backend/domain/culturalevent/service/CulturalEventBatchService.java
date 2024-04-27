package project.backend.domain.culturalevent.service;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import project.backend.domain.culturalevent.repository.CulturalEventRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CulturalEventBatchService {

    private final CulturalEventRepository culturalEventRepository;

    /**
     * interpark의 최신 goodsCode 가져오기
     * @return List
     */
    public List<String> interparkGoodsCodeList() {
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


}
