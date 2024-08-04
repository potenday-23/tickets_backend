package project.backend.domain.sticker.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.backend.domain.sticker.dto.StickerRetrieveDto;
import project.backend.domain.sticker.entity.Sticker;
import project.backend.domain.sticker.mapper.StickerMapper;
import project.backend.domain.sticker.service.StickerService;

import java.util.List;

@Api(tags = "스티커 API")
@RestController
@RequestMapping("/api/stickers")
@RequiredArgsConstructor
public class StickerController {

    private final StickerService stickerService;
    private final StickerMapper stickerMapper;

    @ApiOperation(value = "스티커 리스트 조회")
    @GetMapping
    public ResponseEntity getSticker() {
        List<Sticker> stickers = stickerService.getStickerList();
        List<StickerRetrieveDto> stickerRetrieveDtos = stickerMapper.stickersToStickerRetrieveDto(stickers);
        return ResponseEntity.status(HttpStatus.OK).body(stickerRetrieveDtos);
    }
}
