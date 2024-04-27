package project.backend.domain.culturalevnetcategory.dto;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CulturalEventCategoryResponseDto {
    private String title;
    private String ordering;
    private String imageUrl;
}