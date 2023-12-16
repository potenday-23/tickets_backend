package project.backend.domain.suspend.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuspendPatchRequestDto {
    public String title;
    public String content;
}
