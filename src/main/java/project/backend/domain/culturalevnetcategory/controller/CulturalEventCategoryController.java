package project.backend.domain.culturalevnetcategory.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.backend.domain.culturalevnetcategory.dto.CulturalEventCategoryListDto;
import project.backend.domain.culturalevnetcategory.entity.CulturalEventCategory;
import project.backend.domain.culturalevnetcategory.mapper.CulturalEventCategoryMapper;
import project.backend.domain.culturalevnetcategory.service.CulturalEventCategoryService;

import java.util.List;

@Api(tags = "A. 문화생활 - 카테고리")
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
