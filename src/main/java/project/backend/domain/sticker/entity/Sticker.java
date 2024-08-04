package project.backend.domain.sticker.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.backend.domain.common.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Sticker extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String title;

    @Column(length = 2048)
    public String imageUrl;

    @Builder
    public Sticker(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
