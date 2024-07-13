package project.backend.domain.member.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import project.backend.domain.member.dto.*;
import project.backend.domain.member.entity.Member;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {
    MemberResponseDto memberToMemberResponseDto(Member member);

    MemberRetrieveDto memberToMemberRetrieveDto(Member member);
}
