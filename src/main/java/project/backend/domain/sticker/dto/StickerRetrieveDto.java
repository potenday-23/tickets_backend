package project.backend.domain.sticker.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StickerRetrieveDto {
    public String title;
    public String content;
}
