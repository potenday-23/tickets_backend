package project.backend.domain.keyword.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CulturalEventPopularKeywordListDto {
    private Integer ordering;
    private String keyword;
}