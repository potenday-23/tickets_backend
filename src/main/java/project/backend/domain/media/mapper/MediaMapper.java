package project.backend.domain.media.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import project.backend.domain.media.dto.MediaDto;
import project.backend.domain.media.entity.Media;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MediaMapper {
    MediaDto mediaToMediaDto(Media media);
}
