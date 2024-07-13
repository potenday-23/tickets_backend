package project.backend.domain.memberTicketLike.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import project.backend.domain.memberTicketLike.dto.MemberTicketLikeResponseDto;
import project.backend.domain.memberTicketLike.entity.MemberTicketLike;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberTicketLikeMapper {
    MemberTicketLikeResponseDto memberTicketLikeToMemberTicketLikeResponseDto(MemberTicketLike memberTicketLike);
}
