package project.backend.domain.culturalevnetcategory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.backend.domain.culturalevnetcategory.entity.CategoryTitle;
import project.backend.domain.culturalevnetcategory.entity.CulturalEventCategory;
import project.backend.domain.place.entity.Place;

import java.util.Optional;

public interface CulturalEventCategoryRepository extends JpaRepository<CulturalEventCategory, Long> {
    Optional<CulturalEventCategory> findFirstByTitle(CategoryTitle title);

}
