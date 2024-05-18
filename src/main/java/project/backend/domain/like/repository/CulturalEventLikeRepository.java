package project.backend.domain.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.backend.domain.like.entity.CulturalEventLike;

public interface CulturalEventLikeRepository extends JpaRepository<CulturalEventLike, Long> {
}
