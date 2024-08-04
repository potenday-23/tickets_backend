package project.backend.domain.sticker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import project.backend.domain.sticker.dto.StickerRetrieveDto;
import project.backend.domain.sticker.entity.Sticker;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StickerMapper {

    List<StickerRetrieveDto> stickersToStickerRetrieveDto(List<Sticker> sticker);
}
