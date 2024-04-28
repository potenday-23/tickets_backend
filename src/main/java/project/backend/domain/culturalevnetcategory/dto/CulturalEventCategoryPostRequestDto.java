package project.backend.domain.culturalevnetcategory.dto;

import lombok.*;
import project.backend.domain.culturalevnetcategory.entity.CategoryTitle;

import javax.persistence.Column;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CulturalEventCategoryPostRequestDto {
    private CategoryTitle title;
    private Integer ordering;
    private String imageUrl;
}