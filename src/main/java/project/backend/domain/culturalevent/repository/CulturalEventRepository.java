package project.backend.domain.culturalevent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.backend.domain.culturalevent.entity.CulturalEvent;
import project.backend.domain.place.entity.Place;

import java.util.Optional;

public interface CulturalEventRepository extends JpaRepository<CulturalEvent, Long> {
    Optional<CulturalEvent> findFirstByTitle(String title);

}
