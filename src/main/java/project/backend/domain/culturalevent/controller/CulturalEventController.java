package project.backend.domain.culturalevent.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.backend.domain.culturalevent.dto.CulturalEventListDto;
import project.backend.domain.culturalevent.dto.CulturalEventResponseDto;
import project.backend.domain.culturalevent.mapper.CulturalEventMapper;
import project.backend.domain.culturalevent.service.CulturalEventBatchService;
import project.backend.domain.culturalevent.service.CulturalEventService;
import project.backend.domain.culturalevnetcategory.entity.CategoryTitle;
import project.backend.domain.notice.dto.NoticeResponseDto;
import project.backend.domain.notice.mapper.NoticeMapper;
import project.backend.domain.notice.service.NoticeService;
import project.backend.domain.place.service.PlaceService;

import java.util.List;

@Api(tags = "A. 문화생활")
@RestController
@RequestMapping("/api/cultural-events")
@RequiredArgsConstructor
public class CulturalEventController {

    private final CulturalEventService culturalEventService;
    private final CulturalEventMapper culturalEventMapper;

    @ApiOperation(value = "문화생활 리스트 조회")
    @GetMapping
    public ResponseEntity getCulturalEventList(
            @RequestParam() CategoryTitle type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<CulturalEventListDto> CulturalEventResponseDtoList = culturalEventMapper
                .culturalEventToCulturalEventListDtos(culturalEventService.getCulturalEventList(type, page, size));
        return ResponseEntity.status(HttpStatus.OK).body(CulturalEventResponseDtoList);
    }
}
