package project.backend.domain.category.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.backend.domain.category.dto.CategoryPatchRequestDto;
import project.backend.domain.category.dto.CategoryPostRequestDto;
import project.backend.domain.category.entity.Category;
import project.backend.domain.category.mapper.CategoryMapper;
import project.backend.domain.category.service.CategoryService;
import project.backend.global.s3.service.ImageService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/categorys")
@RequiredArgsConstructor
@Api(tags = "카테고리 API")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;
    private final ImageService imageService;


    @ApiOperation(
            value = "카테고리 목록 조회",
            notes = "카테고리 목록을 조회한다.")
    @GetMapping
    public ResponseEntity getCategoryList() {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategoryList());
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "multipart/form-data")
    public ResponseEntity postCategory(
            @RequestPart(value = "basicImage", required = false) MultipartFile basicImage,
            @RequestPart(value = "clickImage", required = false) MultipartFile clickImage,
            @Valid @RequestPart(required = false) CategoryPostRequestDto request) {

        if (!ObjectUtils.isEmpty(basicImage)) {
            request.setBasicImage(imageService.updateCategoryImage(basicImage, "Category", "basicImage"));
        } if (!ObjectUtils.isEmpty(clickImage)) {
            request.setClickImage(imageService.updateCategoryImage(clickImage, "Category", "clickImage"));
        }

        // Ticket 생성
        Category category = categoryService.createCategory(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(categoryMapper.categoryToCategoryResponseDto(category));
    }

    @RequestMapping(method = RequestMethod.PATCH, consumes = "multipart/form-data")
    public ResponseEntity patchCategory(
            @RequestParam(value = "name", required = false) String name,
            @RequestPart(value = "basicImage", required = false) MultipartFile basicImage,
            @RequestPart(value = "clickImage", required = false) MultipartFile clickImage,
            @Valid @RequestPart(required = false) CategoryPatchRequestDto request) {

        if (!ObjectUtils.isEmpty(basicImage)) {
            request.setBasicImage(imageService.updateCategoryImage(basicImage, "Category", "basicImage"));
        } if (!ObjectUtils.isEmpty(clickImage)) {
            request.setClickImage(imageService.updateCategoryImage(clickImage, "Category", "clickImage"));
        }

        // Ticket 생성
        Category category = categoryService.updateCategory(request, name);

        return ResponseEntity.status(HttpStatus.CREATED).body(categoryMapper.categoryToCategoryResponseDto(category));
    }

    @DeleteMapping
    public ResponseEntity deleteCategory(
            @RequestParam(value = "name", required = false) String name) {

        categoryService.deleteCategory(name);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
