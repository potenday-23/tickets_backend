package project.backend.domain.media.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.backend.domain.media.entity.Media;
import project.backend.domain.media.mapper.MediaMapper;
import project.backend.domain.media.service.MediaService;

@Api(tags = "C. 미디어")
@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;
    private final MediaMapper mediaMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "미디어 생성")
    public ResponseEntity createMedia(
            @RequestPart(value = "file", required = true) MultipartFile file
    ) {
        Media media = mediaService.createMedia(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(mediaMapper.mediaToMediaDto(media));
    }
}
