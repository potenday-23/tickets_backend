package project.backend.domain.notice.dto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeResponseDto {
    public String title;
    public String content;
    public LocalDateTime createdDate;
    public LocalDateTime updatedDate;
}