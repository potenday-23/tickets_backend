package project.backend.domain.suspend.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuspendPostRequestDto {
    public int suspendPeriod;
    public String suspendReason;
}