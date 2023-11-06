package project.backend.domain.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import project.backend.domain.category.entity.Category;
import project.backend.domain.category.repository.CategoryRepository;

@RequiredArgsConstructor
@Component
public class CategoryInitService implements ApplicationRunner {

    private final CategoryRepository categoryRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(categoryRepository.findAll().size() == 0) {
            categoryRepository.save(Category.builder().name("영화").build());
            categoryRepository.save(Category.builder().name("뮤지컬").build());
            categoryRepository.save(Category.builder().name("드라마").build());
            categoryRepository.save(Category.builder().name("전시회").build());
            categoryRepository.save(Category.builder().name("영화").build());
            categoryRepository.save(Category.builder().name("팝업스토어").build());
            categoryRepository.save(Category.builder().name("스포츠 경기").build());
            categoryRepository.save(Category.builder().name("독서").build());
            categoryRepository.save(Category.builder().name("기타").build());
        }
    }
}