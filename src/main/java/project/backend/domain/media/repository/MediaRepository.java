package project.backend.domain.media.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.backend.domain.media.entity.Media;

public interface MediaRepository extends JpaRepository<Media, Long> {
}
