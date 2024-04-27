package project.backend.domain.quit.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.backend.domain.common.entity.BaseEntity;
import project.backend.domain.quit.dto.QuitPatchRequestDto;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Quit extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String reason;

    public Integer count;

    @Builder
    public Quit(String reason){
        this.reason = reason;
        this.count = 0;
    }

    // Patch
    public Quit patchQuit(QuitPatchRequestDto quitPatchRequestDto){
        this.reason = Optional.ofNullable(quitPatchRequestDto.getReason()).orElse(this.reason);
        this.count = (quitPatchRequestDto.getCount() != null && quitPatchRequestDto.getCount() != 0) ? quitPatchRequestDto.getCount() : this.count;
        return this;
    }
}
