package project.backend.domain.culturalevnetcategory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.backend.domain.culturalevnetcategory.entity.CulturalEventCategory;

public interface CulturalEventCategoryRepository extends JpaRepository<CulturalEventCategory, Long> {
}
