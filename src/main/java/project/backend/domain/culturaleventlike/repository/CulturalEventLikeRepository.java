package project.backend.domain.culturaleventlike.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.backend.domain.culturaleventlike.entity.CulturalEventLike;

public interface CulturalEventLikeRepository extends JpaRepository<CulturalEventLike, Long> {
}
