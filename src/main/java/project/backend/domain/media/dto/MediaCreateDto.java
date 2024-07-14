package project.backend.domain.media.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaCreateDto {
    private String mediaUrl;
}