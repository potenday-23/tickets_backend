package project.backend.domain.sticker.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StickerRetrieveDto {
    public Long id;
    public String title;
    public String imageUrl;
}
