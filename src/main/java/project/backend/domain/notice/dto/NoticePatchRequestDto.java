package project.backend.domain.notice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticePatchRequestDto {
    public String title;
    public String content;
}
