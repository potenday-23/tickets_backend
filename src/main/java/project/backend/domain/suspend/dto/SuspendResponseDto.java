package project.backend.domain.suspend.dto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuspendResponseDto {
    public String title;
    public String content;
    public LocalDateTime createdDate;
    public LocalDateTime updatedDate;
}