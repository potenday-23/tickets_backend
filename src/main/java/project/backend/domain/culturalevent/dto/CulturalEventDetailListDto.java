package project.backend.domain.culturalevent.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CulturalEventDetailListDto {
    private String searchKeyword;
    private String resultKeyword;
    private Boolean isKeywordSame;
    private List<CulturalEventListDto> culturalEvents;
}