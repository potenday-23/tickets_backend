package project.backend.domain.notice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import project.backend.domain.culturalevent.service.CulturalEventBatchService;
import project.backend.domain.notice.dto.NoticePostRequestDto;
import project.backend.domain.notice.dto.NoticeResponseDto;
import project.backend.domain.notice.dto.NoticePatchRequestDto;
import project.backend.domain.notice.entity.Notice;
import project.backend.domain.notice.mapper.NoticeMapper;
import project.backend.domain.notice.service.NoticeService;
import project.backend.global.error.exception.BusinessException;
import project.backend.global.error.exception.ErrorCode;

import java.util.List;

@Api(tags = "공지 API")
@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;
    private final NoticeMapper noticeMapper;
    private final CulturalEventBatchService culturalEventBatchService;


    @PostMapping
    public ResponseEntity postNotice(@RequestBody(required = false) NoticePostRequestDto noticePostRequestDto) {
        if (ObjectUtils.isEmpty(noticePostRequestDto)){
            throw new BusinessException(ErrorCode.MISSING_REQUEST);
        }
        Notice notice = noticeService.createNotice(noticePostRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(noticeMapper.noticeToNoticeResponseDto(notice));
    }

    @ApiOperation(value = "공지 목록")
    @GetMapping("/{noticeId}")
    public ResponseEntity getNotice(@PathVariable(required = false) Long noticeId) {
        if (ObjectUtils.isEmpty(noticeId)){
            throw new BusinessException(ErrorCode.MISSING_REQUEST);
        }
        NoticeResponseDto noticeResponseDto = noticeMapper.noticeToNoticeResponseDto(noticeService.getNotice(noticeId));
        return ResponseEntity.status(HttpStatus.OK).body(noticeResponseDto);
    }

    @GetMapping
    public ResponseEntity getNoticeList() {
        culturalEventBatchService.interparkGoodsCodeList();
        List<NoticeResponseDto> noticeResponseDtoList = noticeMapper.noticesToNoticeResponseDtos(noticeService.getNoticeList());
        return ResponseEntity.status(HttpStatus.OK).body(noticeResponseDtoList);
    }

    @PatchMapping("/{noticeId}")
    public ResponseEntity putNotice(
            @PathVariable(required = false) Long noticeId,
            @RequestBody(required = false) NoticePatchRequestDto noticePatchRequestDto) {
        if (ObjectUtils.isEmpty(noticeId) || ObjectUtils.isEmpty(noticePatchRequestDto)){
            throw new BusinessException(ErrorCode.MISSING_REQUEST);
        }
        NoticeResponseDto noticeResponseDto = noticeMapper.noticeToNoticeResponseDto(noticeService.patchNotice(noticeId, noticePatchRequestDto));
        return ResponseEntity.status(HttpStatus.OK).body(noticeResponseDto);
    }

    @DeleteMapping("/{noticeId}")
    public ResponseEntity deleteNotice(@PathVariable(required = false) Long noticeId) {
        if (ObjectUtils.isEmpty(noticeId)){
            throw new BusinessException(ErrorCode.MISSING_REQUEST);
        }
        noticeService.deleteNotice(noticeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
