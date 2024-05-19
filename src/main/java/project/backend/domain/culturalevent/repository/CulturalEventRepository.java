package project.backend.domain.culturalevent.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.backend.domain.culturalevent.entity.CulturalEvent;

import java.util.Optional;

public interface CulturalEventRepository extends JpaRepository<CulturalEvent, Long>, CulturalEventRepositoryCustom {
    Optional<CulturalEvent> findFirstByTitle(String title);
    Page<CulturalEvent> findAll(Pageable pageable);
}
