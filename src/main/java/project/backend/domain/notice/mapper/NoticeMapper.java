package project.backend.domain.notice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import project.backend.domain.notice.dto.NoticePatchRequestDto;
import project.backend.domain.notice.dto.NoticePostRequestDto;
import project.backend.domain.notice.dto.NoticeResponseDto;
import project.backend.domain.notice.entity.Notice;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NoticeMapper {
    Notice noticePostRequestDtoToNotice(NoticePostRequestDto noticePostRequestDto);

    Notice noticePatchRequestDtoToNotice(NoticePatchRequestDto noticePatchRequestDto);

    NoticeResponseDto noticeToNoticeResponseDto(Notice notice);

    List<NoticeResponseDto> noticesToNoticeResponseDtos(List<Notice> notice);
}
