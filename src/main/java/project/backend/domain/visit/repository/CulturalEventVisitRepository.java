package project.backend.domain.visit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.backend.domain.visit.entity.CulturalEventVisit;

public interface CulturalEventVisitRepository extends JpaRepository<CulturalEventVisit, Long> {
}
