package project.backend.domain.culturaleventevalutaion.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.backend.domain.culturalevent.dto.CulturalEventRetrieveDto;
import project.backend.domain.culturalevent.entity.CulturalEvent;
import project.backend.domain.culturalevent.mapper.CulturalEventMapper;
import project.backend.domain.culturalevent.service.CulturalEventService;
import project.backend.domain.culturaleventevalutaion.dto.CulturalEventEvaluationListDto;
import project.backend.domain.culturaleventevalutaion.mapper.CulturalEventEvaluationMapper;
import project.backend.domain.culturaleventevalutaion.service.CulturalEventEvaluationService;
import project.backend.domain.culturalevnetcategory.entity.CategoryTitle;
import project.backend.domain.culturalevnetinfo.service.CulturalEventInfoService;
import project.backend.domain.ticketingsite.mapper.TicketingSiteMapper;

import javax.validation.constraints.Positive;
import java.util.List;

@Api(tags = "A. 문화생활 - 평가")
@RestController
@RequestMapping("/api/cultural-event-evaluations")
@RequiredArgsConstructor
public class CulturalEventEvaluationController {

    private final CulturalEventEvaluationService culturalEventEvaluationService;
    private final CulturalEventEvaluationMapper culturalEventEvaluationMapper;

    @ApiOperation(value = "문화생활 평가 리스트 조회")
    @GetMapping
    public ResponseEntity getCulturalEventEvaluationList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<CulturalEventEvaluationListDto> culturalEventEvaluationListDtoList = culturalEventEvaluationMapper
                .culturalEventEvaluationToCulturalEventEvaluationListDtos(culturalEventEvaluationService.getCulturalEventEvaluations(page, size));
        return ResponseEntity.status(HttpStatus.OK).body(culturalEventEvaluationListDtoList);
    }
}


