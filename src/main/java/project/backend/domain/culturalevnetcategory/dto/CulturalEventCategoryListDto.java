package project.backend.domain.culturalevnetcategory.dto;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CulturalEventCategoryListDto {

    private Integer id;
    private String type;
    private String name;
}