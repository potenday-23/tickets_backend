package project.backend.domain.culturalevent.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.backend.domain.culturalevent.dto.CulturalEventListDto;
import project.backend.domain.culturalevent.dto.CulturalEventRetrieveDto;
import project.backend.domain.culturalevent.entity.CulturalEvent;
import project.backend.domain.culturalevent.mapper.CulturalEventMapper;
import project.backend.domain.culturalevent.service.CulturalEventService;
import project.backend.domain.culturalevnetcategory.entity.CategoryTitle;
import project.backend.domain.culturalevnetinfo.service.CulturalEventInfoService;
import project.backend.domain.ticketingsite.mapper.TicketingSiteMapper;
import project.backend.domain.member.service.MemberJwtService;

import javax.validation.constraints.Positive;
import java.util.List;

@Api(tags = "A. 문화생활")
@RestController
@RequestMapping("/api/cultural-events")
@RequiredArgsConstructor
public class CulturalEventController {

    private final CulturalEventService culturalEventService;
    private final CulturalEventMapper culturalEventMapper;
    private final CulturalEventInfoService culturalEventInfoService;
    private final TicketingSiteMapper ticketingSiteMapper;

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

    @ApiOperation(value = "문화생활 객체 조회")
    @GetMapping("/{id}")
    public ResponseEntity getCulturalEvent(@Positive @PathVariable Long id) {
        CulturalEvent culturalEvent = culturalEventService.getCulturalEvent(id);
        CulturalEventRetrieveDto culturalEventRetrieveDto = culturalEventMapper.culturalEventToCulturalEventRetrieveDto(culturalEvent);
        // information 추가
        culturalEventRetrieveDto.setInformationList(culturalEventInfoService.getImageUrlList(culturalEvent));

        // ticketingSite 추가
        culturalEventRetrieveDto.setTicketingSiteList(ticketingSiteMapper.ticketingSiteListToTicketingSiteListDtos(culturalEvent.getTicketingSiteList()));
        return ResponseEntity.status(HttpStatus.OK).body(culturalEventRetrieveDto);
    }

    @ApiOperation(value = "문화생활 좋아요")
    @PostMapping("/{id}/like")
    public ResponseEntity likeCulturalEvent(@Positive @PathVariable Long id) {
        culturalEventService.like(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @ApiOperation(value = "문화생활 좋아요 취소")
    @PostMapping("/{id}/unlike")
    public ResponseEntity unLikeCulturalEvent(@Positive @PathVariable Long id) {
        culturalEventService.unLike(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}


