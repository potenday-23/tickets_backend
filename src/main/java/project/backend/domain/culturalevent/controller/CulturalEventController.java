package project.backend.domain.culturalevent.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.backend.domain.culturalevent.dto.CulturalEventListDto;
import project.backend.domain.culturalevent.dto.CulturalEventRetrieveDto;
import project.backend.domain.culturalevent.entity.CulturalEvent;
import project.backend.domain.culturalevent.mapper.CulturalEventMapper;
import project.backend.domain.culturalevent.service.CulturalEventService;
import project.backend.domain.culturalevnetcategory.entity.CategoryTitle;
import project.backend.domain.culturalevnetinfo.service.CulturalEventInfoService;
import project.backend.domain.member.entity.Member;
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
    private final MemberJwtService memberJwtService;

    @ApiOperation(value = "문화생활 리스트 조회",
            notes = " - ordering : point(인기순)")
    @GetMapping
    public ResponseEntity getCulturalEventList(
            @RequestParam() CategoryTitle type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String ordering
            ) {
        // Member
        Member member = memberJwtService.getMember();

        // Response
        List<CulturalEvent> culturalEventList = culturalEventService.getCulturalEventList(type, page, size, ordering);
        List<CulturalEventListDto> culturalEventResponseDtoList = culturalEventMapper
                .culturalEventToCulturalEventListDtos(culturalEventList);
        culturalEventResponseDtoList.forEach(dto -> {
            dto.setIsOpened();
            dto.setIsLiked(member);
        });
        return ResponseEntity.status(HttpStatus.OK).body(culturalEventResponseDtoList);
    }

    @ApiOperation(value = "문화생활 객체 조회")
    @GetMapping("/{id}")
    public ResponseEntity getCulturalEvent(@Positive @PathVariable Long id) {

        // Member
        Member member = memberJwtService.getMember();
        CulturalEvent culturalEvent = culturalEventService.getCulturalEvent(id);

        // Make Visit
        culturalEventService.visit(culturalEvent);

        // CulturalEventRetrieveDto
        CulturalEventRetrieveDto culturalEventRetrieveDto = culturalEventMapper.culturalEventToCulturalEventRetrieveDto(culturalEvent);
        culturalEventRetrieveDto.setIsLiked(member);
        culturalEventRetrieveDto.setIsOpened();
        culturalEventRetrieveDto.setInformationList(culturalEventInfoService.getImageUrlList(culturalEvent));
        culturalEventRetrieveDto.setTicketingSiteList(ticketingSiteMapper.ticketingSiteListToTicketingSiteListDtos(culturalEvent.getTicketingSiteList()));

        return ResponseEntity.status(HttpStatus.OK).body(culturalEventRetrieveDto);
    }

    @ApiOperation(value = "문화생활 좋아요")
    @PostMapping("/{id}/like")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity likeCulturalEvent(@Positive @PathVariable Long id) {
        culturalEventService.like(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @ApiOperation(value = "문화생활 좋아요 취소")
    @PostMapping("/{id}/unlike")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity unLikeCulturalEvent(@Positive @PathVariable Long id) {
        culturalEventService.unLike(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}


