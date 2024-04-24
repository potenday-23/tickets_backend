package project.backend.domain.culturalevent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.backend.domain.culturalevent.entity.CulturalEvent;

public interface CulturalEventRepository extends JpaRepository<CulturalEvent, Long> {
}
