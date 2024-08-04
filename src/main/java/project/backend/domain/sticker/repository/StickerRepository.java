package project.backend.domain.sticker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.backend.domain.sticker.entity.Sticker;

public interface StickerRepository extends JpaRepository<Sticker, Long> {
}
