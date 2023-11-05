package project.backend.domain.ticket.dto;

import lombok.*;
import project.backend.domain.member.entity.Member;
import project.backend.domain.ticket.entity.IsPrivate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketPostRequestDto {
    public String imageUrl;
    public String ticketImageUrl;
    @NotNull(message = "날짜를 입력해야 합니다.")
    public LocalDateTime ticketDate;
    @Min(value = 0, message = "0보다 작은 값은 별점으로 설정할 수 없습니다.")
    @Max(value = 5, message = "5보다 큰 값은 별점으로 설정할 수 없습니다.")
    public Float rating; // todo : 필수로 입력받도록 변경하기
    @NotNull(message = "메모를 입력해야 합니다.")
    public String memo;
    public String seat;
    public String location;
    @Min(value = 0, message = "0보다 작은 가격은 설정할 수 없습니다.")
    public Integer price;
    public String friend;
    public IsPrivate isPrivate;
    public Member member;
    @NotNull(message = "카테고리명을 선택해야 합니다.")
    public String categoryName;
}
