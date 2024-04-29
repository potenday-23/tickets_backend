package project.backend.domain.culturalevnetcategory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import project.backend.domain.category.entity.Category;
import project.backend.domain.culturalevnetcategory.entity.CategoryTitle;
import project.backend.domain.culturalevnetcategory.entity.CulturalEventCategory;
import project.backend.domain.culturalevnetcategory.repository.CulturalEventCategoryRepository;
import project.backend.domain.quit.entity.Quit;
import project.backend.domain.quit.repository.QuitRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class CulturalCategoryInitService implements ApplicationRunner {

    private final CulturalEventCategoryRepository culturalEventCategoryRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<CulturalEventCategory> culturalEventCategoryList = new ArrayList<>();
        for (CategoryTitle categoryTitle : CategoryTitle.values()) {
            if (!culturalEventCategoryRepository.findFirstByTitle(categoryTitle).isPresent()) {
                culturalEventCategoryList.add(CulturalEventCategory.builder()
                        .title(categoryTitle)
                        .ordering(categoryTitle.getOrdering()).build());
            }
        }
        culturalEventCategoryRepository.saveAll(culturalEventCategoryList);

    }
}