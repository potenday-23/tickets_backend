package project.backend.domain.culturaleventcategory.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.backend.domain.culturaleventcategory.dto.CulturalEventCategoryListDto;
import project.backend.domain.culturaleventcategory.entity.CulturalEventCategory;
import project.backend.domain.culturaleventcategory.mapper.CulturalEventCategoryMapper;
import project.backend.domain.culturaleventcategory.service.CulturalEventCategoryService;

import java.util.List;

@Api(tags = "CulturalEvent - 문화생활 카테고리")
@RestController
@RequestMapping("/api/categories/cultural-events")
@RequiredArgsConstructor
public class CulturalEventCategoryController {

    private final CulturalEventCategoryService culturalEventCategoryService;
    private final CulturalEventCategoryMapper culturalEventCategoryMapper;

    @ApiOperation(value = "문화 생활 카테고리 리스트 조회")
    @GetMapping
    public ResponseEntity getCulturalEventCategoryList() {
        List<CulturalEventCategory> culturalEventCategoryList = culturalEventCategoryService.getCulturalEventCategoryList();
        List<CulturalEventCategoryListDto> culturalEventResponseDtoList = culturalEventCategoryMapper
                .culturalEventCategoryToCulturalEventCategoryResponseDtos(culturalEventCategoryList);
        return ResponseEntity.status(HttpStatus.OK).body(culturalEventResponseDtoList);
    }
}
