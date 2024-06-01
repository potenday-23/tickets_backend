package project.backend.domain.culturalevent.repository;

import project.backend.domain.culturalevent.entity.CulturalEvent;
import project.backend.domain.culturalevnetcategory.entity.CategoryTitle;

import java.util.List;

public interface CulturalEventRepositoryCustom {
    List<CulturalEvent> getCulturalEventList(int page, int size, List<CategoryTitle> categories, String ordering, Boolean isOpened, Double latitude, Double longitude, String keyword);

    List<CulturalEvent> getCulturalEventSearchList(int page, int size, String keyword);

    List<CulturalEvent> getMemberCulturalEventList();
}
