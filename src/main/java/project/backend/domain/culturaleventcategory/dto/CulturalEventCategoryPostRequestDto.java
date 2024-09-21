package project.backend.domain.culturaleventcategory.dto;

import lombok.*;
import project.backend.domain.culturaleventcategory.entity.CategoryTitle;

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