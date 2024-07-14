package project.backend.domain.media.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.backend.domain.media.entity.Media;
import project.backend.domain.media.mapper.MediaMapper;
import project.backend.domain.media.repository.MediaRepository;
import project.backend.global.error.exception.BusinessException;
import project.backend.global.error.exception.ErrorCode;
import project.backend.global.s3.service.ImageService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MediaService {
    private final MediaRepository mediaRepository;
    private final MediaMapper mediaMapper;
    private final ImageService imageService;

    public Media createMedia(MultipartFile file) {
        String media_url = imageService.updateImage(file, "Media", "media_url");
        Media media = Media.builder().media_url(media_url).build();
        mediaRepository.save(media);
        return media;
    }
}
