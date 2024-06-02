package project.backend.domain.keyword.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CulturalEventSearchKeywordListDto {
    private Long id;
    private String keyword;
    private Integer ordering;
}