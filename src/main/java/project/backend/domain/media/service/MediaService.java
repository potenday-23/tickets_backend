package project.backend.domain.media.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.backend.domain.media.dto.MediaDto;
import project.backend.domain.media.entity.Media;
import project.backend.domain.media.mapper.MediaMapper;
import project.backend.domain.media.repository.MediaRepository;
import project.backend.global.error.exception.BusinessException;
import project.backend.global.error.exception.ErrorCode;
import project.backend.global.s3.service.ImageService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MediaService {
    private final MediaRepository mediaRepository;
    private final MediaMapper mediaMapper;
    private final ImageService imageService;

    public Media createMedia(MultipartFile file) {
        String mediaUrl = imageService.updateImage(file, "Media", "mediaUrl");
        Media media = Media.builder().mediaUrl(mediaUrl).build();
        mediaRepository.save(media);
        return media;
    }

    public Media setOrderThumbnail(MediaDto mediaDto) {
        Media media;
        Optional<Media> optionalMedia = mediaRepository.findFirstByMediaUrl(mediaDto.getMediaUrl());
        if (optionalMedia.isPresent()) {
            media = optionalMedia.get();
        } else {
            media = Media.builder().mediaUrl(mediaDto.getMediaUrl()).build();
        }
        media.setOrdering(mediaDto.getOrdering());
        media.setIsThumbnail(mediaDto.getIsThumbnail());
        return mediaRepository.save(media);
    }
}
