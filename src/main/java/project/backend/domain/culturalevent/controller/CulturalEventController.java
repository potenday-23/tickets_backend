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
import project.backend.domain.culturalevent.dto.CulturalEventSearchListDto;
import project.backend.domain.culturalevent.entity.CulturalEvent;
import project.backend.domain.culturalevent.mapper.CulturalEventMapper;
import project.backend.domain.culturalevent.service.CulturalEventService;
import project.backend.domain.culturalevnetcategory.entity.CategoryTitle;
import project.backend.domain.culturalevnetinfo.service.CulturalEventInfoService;
import project.backend.domain.keyword.dto.CulturalEventPopularKeywordListDto;
import project.backend.domain.keyword.dto.CulturalEventSearchKeywordListDto;
import project.backend.domain.keyword.entity.CulturalEventSearchKeyword;
import project.backend.domain.keyword.mapper.CulturalEventSearchKeywordMapper;
import project.backend.domain.keyword.service.CulturalEventSearchKeywordService;
import project.backend.domain.member.entity.Member;
import project.backend.domain.ticketingsite.mapper.TicketingSiteMapper;
import project.backend.domain.member.service.MemberJwtService;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.IntStream;

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
    private final CulturalEventSearchKeywordService culturalEventSearchKeywordService;
    private final CulturalEventSearchKeywordMapper culturalEventSearchKeywordMapper;

    @ApiOperation(value = "문화생활 리스트 조회",
            notes = "`ordering` : ticketOpenDate(오픈 다가온 순) | -point(인기순) | -updatedDate(최근순) | recommend(추천순) | endDate(공연마감순)\n" +
                    "`latitude` : 입력시 추천순 정렬에 반경 50km 이내 문화생활 적용\n" +
                    "`longitude` : 입력시 추천순 정렬에 반경 50km 이내 문화생활 적용")
    @GetMapping
    public ResponseEntity getCulturalEventList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String ordering,
            @RequestParam(required = false) Boolean isOpened,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false) List<CategoryTitle> categories,
            @RequestParam(required = false) String keyword
    ) {
        // Member
        Member member = memberJwtService.getMember();

        // Save Search Keyword
        if (keyword != null) {
            culturalEventSearchKeywordService.createCulturalEventSearchKeyword(member, keyword);
        }

        // Get Cultural Event
        List<CulturalEvent> culturalEventList = culturalEventService.getCulturalEventList(page, size, categories, ordering, isOpened, latitude, longitude, keyword);
        List<CulturalEventListDto> culturalEventResponseDtoList = culturalEventMapper
                .culturalEventToCulturalEventListDtos(culturalEventList);
        culturalEventResponseDtoList.forEach(dto -> {
            dto.setIsOpened();
            dto.setIsLiked(member);
        });
        return ResponseEntity.status(HttpStatus.OK).body(culturalEventResponseDtoList);
    }

    @ApiOperation(value = "문화생활 검색 리스트 조회")
    @GetMapping("/search")
    public ResponseEntity getCulturalEventSearchList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String keyword
    ) {
        // Response
        List<CulturalEvent> culturalEventList = culturalEventService.getCulturalEventSearchList(page, size, keyword);
        List<CulturalEventSearchListDto> CulturalEventSearchListDtoList = culturalEventMapper.culturalEventToCulturalEventSearchListDtos(culturalEventList);
        return ResponseEntity.status(HttpStatus.OK).body(CulturalEventSearchListDtoList);
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


    @ApiOperation(value = "문화생활 최근 검색어 리스트 조회")
    @GetMapping("/recent-keywords")
    public ResponseEntity<List<CulturalEventSearchKeywordListDto>> getCulturalEventRecentKeywordList() {
        // Get Recent Keywords
        Member member = memberJwtService.getMember();
        List<CulturalEventSearchKeyword> culturalEventRecentKeywordList = culturalEventSearchKeywordService.getCulturalEventRecentKeywordList(member);

        // Get Dto List
        List<CulturalEventSearchKeywordListDto> culturalEventSearchKeywordListDtoList = culturalEventSearchKeywordMapper
                .culturalEventSearchKeywordToCulturalEventSearchKeywordListDto(culturalEventRecentKeywordList);

        // Set Ordering
        IntStream.range(0, culturalEventSearchKeywordListDtoList.size())
                .forEach(i -> culturalEventSearchKeywordListDtoList.get(i).setOrdering(i + 1));

        return ResponseEntity.status(HttpStatus.OK).body(culturalEventSearchKeywordListDtoList);
    }

    @ApiOperation(value = "문화생활 최근 검색어 삭제")
    @DeleteMapping("/recent-keywords/{id}")
    public ResponseEntity deleteCulturalEventRecentKeyword(@Positive @PathVariable Long id) {
        culturalEventSearchKeywordService.deleteCulturalEventSearchKeyword(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @ApiOperation(value = "문화생활 인기 검색어 리스트 조회")
    @GetMapping("/popular-keywords")
    public ResponseEntity<List<CulturalEventPopularKeywordListDto>> getCulturalEventPopularKeywordList() {
        List<CulturalEventPopularKeywordListDto> culturalEventSearchKeywordListDtoList = culturalEventSearchKeywordService.getCulturalEventPopularKeywordList();
        return ResponseEntity.status(HttpStatus.OK).body(culturalEventSearchKeywordListDtoList);
    }
}


