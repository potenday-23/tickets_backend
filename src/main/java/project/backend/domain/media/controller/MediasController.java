package project.backend.domain.media.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import project.backend.domain.media.dto.MediaCreateDto;
import project.backend.domain.media.entity.Media;
import project.backend.domain.media.mapper.MediaMapper;
import project.backend.domain.media.service.MediaService;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "Media - 미디어")
@RestController
@RequestMapping("/api/medias")
@RequiredArgsConstructor
public class MediasController {

    private final MediaService mediaService;
    private final MediaMapper mediaMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "미디어 다중 생성")
    public ResponseEntity createMedias(
            @RequestPart(value = "files", required = true) List<MultipartFile> files
    ) {
        List<Media> medias = files.stream()
                .map(mediaService::createMedia)
                .collect(Collectors.toList());
        List<MediaCreateDto> mediaDtos = medias.stream()
                .map(mediaMapper::mediaToMediaCreateDto)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.CREATED).body(mediaDtos);
    }
}
