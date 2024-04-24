package project.backend.domain.culturalevnetcategory.dto;

import lombok.*;

import javax.persistence.Column;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CulturalEventCategoryPostRequestDto {
    private String title;
    private String order;
    private String imageUrl;
}