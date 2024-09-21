package project.backend.domain.culturaleventcategory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.backend.domain.culturaleventcategory.entity.CategoryTitle;
import project.backend.domain.culturaleventcategory.entity.CulturalEventCategory;

import java.util.Optional;

public interface CulturalEventCategoryRepository extends JpaRepository<CulturalEventCategory, Long> {
    Optional<CulturalEventCategory> findFirstByTitle(CategoryTitle title);

}
