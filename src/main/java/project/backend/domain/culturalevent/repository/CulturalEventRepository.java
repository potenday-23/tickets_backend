package project.backend.domain.culturalevent.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.backend.domain.culturalevent.entity.CulturalEvent;
import project.backend.domain.culturalevnetcategory.entity.CulturalEventCategory;
import project.backend.domain.place.entity.Place;

import java.util.Optional;

public interface CulturalEventRepository extends JpaRepository<CulturalEvent, Long>, CulturalEventRepositoryCustom {
    Optional<CulturalEvent> findFirstByTitle(String title);
    Page<CulturalEvent> findAll(Pageable pageable);
    Page<CulturalEvent> findAllByCulturalEventCategory(Pageable pageable, CulturalEventCategory culturalEventCategory);
}
