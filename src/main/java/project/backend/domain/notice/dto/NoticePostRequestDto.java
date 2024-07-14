package project.backend.domain.notice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticePostRequestDto {
    public String title;
    public String content;
}