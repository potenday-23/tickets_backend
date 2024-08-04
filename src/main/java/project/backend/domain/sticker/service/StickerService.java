package project.backend.domain.sticker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.domain.sticker.entity.Sticker;
import project.backend.domain.sticker.repository.StickerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StickerService {
    private final StickerRepository stickerRepository;

    public List<Sticker> getStickerList() {
        return stickerRepository.findAll();
    }
}
