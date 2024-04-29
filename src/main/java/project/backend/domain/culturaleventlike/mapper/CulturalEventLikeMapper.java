package project.backend.domain.culturaleventlike.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import project.backend.domain.culturaleventlike.dto.CulturalEventLikePatchRequestDto;
import project.backend.domain.culturaleventlike.dto.CulturalEventLikeResponseDto;
import project.backend.domain.culturaleventlike.entity.CulturalEventLike;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CulturalEventLikeMapper {

    CulturalEventLike culturalEventLikePatchRequestDtoToCulturalEventLike(CulturalEventLikePatchRequestDto culturalEventLikePatchRequestDto);

    CulturalEventLikeResponseDto culturalEventLikeToCulturalEventLikeResponseDto(CulturalEventLike culturalEventLike);

    List<CulturalEventLikeResponseDto> culturalEventLikesToCulturalEventLikeResponseDtos(List<CulturalEventLike> culturalEventLike);
}
