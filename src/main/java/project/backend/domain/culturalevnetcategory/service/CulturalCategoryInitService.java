package project.backend.domain.culturalevnetcategory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
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
        if (culturalEventCategoryRepository.findAll().size() == 0) {
            List<CulturalEventCategory> culturalEventCategoryList = new ArrayList<>();

            culturalEventCategoryList.add(CulturalEventCategory.builder().title(CategoryTitle.DRAMA).ordering(1).build());
            culturalEventCategoryList.add(CulturalEventCategory.builder().title(CategoryTitle.MUSICAL).ordering(2).build());
            culturalEventCategoryList.add(CulturalEventCategory.builder().title(CategoryTitle.CONCERT).ordering(3).build());
            culturalEventCategoryList.add(CulturalEventCategory.builder().title(CategoryTitle.EXHIBIT).ordering(4).build());
            culturalEventCategoryList.add(CulturalEventCategory.builder().title(CategoryTitle.MOVIE).ordering(5).build());

            culturalEventCategoryRepository.saveAll(culturalEventCategoryList);
        }
    }
}