package project.backend.domain.culturalevnetinfo.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CulturalEventInfoCreateDto {
    public String imageUrl;

    public String text;

    public Integer ordering;
}