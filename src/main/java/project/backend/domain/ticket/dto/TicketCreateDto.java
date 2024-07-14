package project.backend.domain.ticket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import project.backend.domain.media.dto.MediaDto;
import project.backend.global.annotation.ValidDateFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketCreateDto {
    // TODO(sprint4) : Category, place 연결 미구현 상태

    @NotBlank
    @NotNull
    @Size(min = 1, max = 20, message = "1자 ~ 20자 사이만 입력 가능합니다.")
    @Schema(description = "제목", example = "<뮤지컬> 시카고", required = true)
    public String title;

    @NotBlank
    @NotNull
    @Schema(description = "메인 이미지", example = "https://placehold.co/600x400/png", required = true)
    public String mainImageUrl;

    @NotNull
    @Schema(description = "날짜", example = "2024-01-01", required = true)
    public LocalDate date;

    @NotNull
    @DecimalMin(value = "1.0", inclusive = false, message = "별점은 1.0 ~ 5.0 사이만 입력 가능합니다")
    @DecimalMax(value = "5.0", message = "별점은 1.0 ~ 5.0 사이만 입력 가능합니다")
    @Schema(description = "별점", example = "3.5", required = true)
    public Float score;

    @Schema(description = "별점", example = "2층 2열 8번", required = false)
    public String seat;

    @Schema(description = "가격", example = "156000", required = false)
    public Integer price;

    @Size(max = 500, message = "Review must be up to 500 characters")
    @Schema(description = "리뷰", example = "정말 재미있었어요", required = false)
    private String review;

    public List<MediaDto> medias;
}
