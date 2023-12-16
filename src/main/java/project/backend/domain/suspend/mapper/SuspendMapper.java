package project.backend.domain.suspend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import project.backend.domain.suspend.dto.SuspendPatchRequestDto;
import project.backend.domain.suspend.dto.SuspendPostRequestDto;
import project.backend.domain.suspend.dto.SuspendResponseDto;
import project.backend.domain.suspend.entity.Suspend;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SuspendMapper {
    Suspend noticePostRequestDtoToNotice(SuspendPostRequestDto suspendPostRequestDto);

    Suspend noticePatchRequestDtoToNotice(SuspendPatchRequestDto suspendPatchRequestDto);

    SuspendResponseDto noticeToNoticeResponseDto(Suspend suspend);

    List<SuspendResponseDto> noticesToNoticeResponseDtos(List<Suspend> suspend);
}
