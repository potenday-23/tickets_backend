package project.backend.domain.culturaleventlike.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CulturalEventLikePatchRequestDto {
    public String title;
    public String content;
}
