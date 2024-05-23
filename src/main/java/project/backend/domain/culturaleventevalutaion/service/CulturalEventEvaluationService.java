package project.backend.domain.culturaleventevalutaion.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import project.backend.domain.culturalevent.entity.CulturalEvent;
import project.backend.domain.culturaleventevalutaion.entity.CulturalEventEvaluation;
import project.backend.domain.culturaleventevalutaion.entity.EvaluationType;
import project.backend.domain.culturaleventevalutaion.repository.CulturalEventEvaluationRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CulturalEventEvaluationService {
    private final CulturalEventEvaluationRepository culturalEventEvaluationRepository;


    /**
     * CulturalEventEvaluation 리스트 조회
     * @param page
     * @param size
     * @return
     */
    public List<CulturalEventEvaluation> getCulturalEventEvaluations(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return culturalEventEvaluationRepository.findAll(pageable).getContent();
    }

    /**
     * goodsCode기준 CulturalEventEvaluation 저장
     *
     * @param goodsCode
     * @param culturalEvent
     */
    public void createCulturalEventEvaluations(String goodsCode, CulturalEvent culturalEvent) {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        List<CulturalEventEvaluation> evaluations = new ArrayList<>();

        String baseUrl = "https://api-ticketfront.interpark.com/v1/boards?best=false&notice=false&page=1&pageSize=60&sort=DESC_WRITE_DATE";

        // 관람평(62)
        UriComponentsBuilder expectEvaluationUriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("boardNo", "62")
                .queryParam("goodsCode", goodsCode);

        ResponseEntity<String> expectEvaluationResponse = restTemplate.exchange(
                expectEvaluationUriBuilder.toUriString(),
                HttpMethod.GET,
                null,
                String.class);

        String jsonResponse = expectEvaluationResponse.getBody();
        try {
            String data = objectMapper.readTree(jsonResponse).get("data").toString();
            JSONArray expectEvaluationJsonArray = new JSONArray(data);

            for (int i = 0; i < expectEvaluationJsonArray.length(); i++) {
                JSONObject jsonObject = expectEvaluationJsonArray.getJSONObject(i);
                CulturalEventEvaluation culturalEventEvaluation = CulturalEventEvaluation.builder()
                        .title(jsonObject.getString("title"))
                        .content(jsonObject.getString("content"))
                        .culturalEvent(culturalEvent)
                        .type(EvaluationType.EXPECT).build();

                // 객체를 리스트에 추가
                evaluations.add(culturalEventEvaluation);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        // 기대평 (boardNo : 10)
        baseUrl = "https://api-ticketfront.interpark.com/v1/boards?best=false&notice=false&page=1&pageSize=40&sort=DESC_WRITE_DATE";
        UriComponentsBuilder reviewEvaluationUriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("boardNo", "10")
                .queryParam("goodsCode", goodsCode);

        ResponseEntity<String> reviewEvaluationResponse = restTemplate.exchange(
                reviewEvaluationUriBuilder.toUriString(),
                HttpMethod.GET,
                null,
                String.class);

        String reviewJsonResponse = reviewEvaluationResponse.getBody();

        try {
            String data = objectMapper.readTree(reviewJsonResponse).get("data").toString();
            JSONArray jsonArray = new JSONArray(data);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                CulturalEventEvaluation culturalEventEvaluation = CulturalEventEvaluation.builder()
                        .title(jsonObject.getString("title"))
                        .content(jsonObject.getString("content"))
                        .score(jsonObject.getInt("boomupCount"))
                        .culturalEvent(culturalEvent)
                        .type(EvaluationType.REVIEW).build();

                evaluations.add(culturalEventEvaluation);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<CulturalEventEvaluation> culturalEventEvaluationList = culturalEventEvaluationRepository.saveAll(evaluations);
    }
}
