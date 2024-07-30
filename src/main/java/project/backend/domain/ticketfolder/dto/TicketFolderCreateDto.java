package project.backend.domain.ticketfolder.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketFolderCreateDto {
    @NotBlank
    @NotNull(message = "티켓 폴더의 제목을 입력해주세요")
    @Size(min = 1, max = 12, message = "1자 ~ 12자 사이만 입력 가능합니다.")
    @Schema(description = "제목", example = "서울 공연", required = true)
    public String title;
}