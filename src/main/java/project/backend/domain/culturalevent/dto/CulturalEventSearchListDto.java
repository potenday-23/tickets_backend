package project.backend.domain.culturalevent.dto;

import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CulturalEventSearchListDto {
    private Long id;
    private String title;
}