package project.backend.domain.culturaleventlike.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CulturalEventLikeRequestDto {
    public String title;
    public String content;
}