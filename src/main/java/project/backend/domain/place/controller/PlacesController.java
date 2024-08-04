package project.backend.domain.place.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.backend.domain.place.dto.PlaceRetrieveDto;
import project.backend.domain.place.service.PlaceService;

import java.util.List;

@Api(tags = "Place - 장소")
@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class PlacesController {
    private final PlaceService placeService;

    @ApiOperation(value = "장소 리스트 조회")
    @GetMapping
    public ResponseEntity getPlaces(
            @RequestParam String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<PlaceRetrieveDto> placeRetrieveDtos = placeService.getKakaoPlaces(search, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(placeRetrieveDtos);
    }
}
