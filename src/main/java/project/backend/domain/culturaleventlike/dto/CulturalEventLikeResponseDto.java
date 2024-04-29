package project.backend.domain.culturaleventlike.dto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CulturalEventLikeResponseDto {
    public String title;
    public String content;
    public LocalDateTime createdDate;
    public LocalDateTime updatedDate;
}