package project.backend.domain.notice.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.backend.domain.common.entity.BaseEntity;
import project.backend.domain.notice.dto.NoticePatchRequestDto;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String title;

    public String content;

    @Builder
    public Notice(String title, String content){
        this.title = title;
        this.content = content;
    }

    // Patch
    public Notice patchNotice(NoticePatchRequestDto noticePatchRequestDto){
        this.title = Optional.ofNullable(noticePatchRequestDto.getTitle()).orElse(this.title);
        this.content = Optional.ofNullable(noticePatchRequestDto.getContent()).orElse(this.content);
        return this;
    }
}
