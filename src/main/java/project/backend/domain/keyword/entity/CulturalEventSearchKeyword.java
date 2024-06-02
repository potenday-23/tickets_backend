package project.backend.domain.keyword.entity;

import lombok.*;
import project.backend.domain.common.entity.BaseEntity;
import project.backend.domain.member.entity.Member;

import javax.persistence.*;
import java.util.Optional;


@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CulturalEventSearchKeyword extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String keyword;

    public Boolean isRecent;

    @ManyToOne(fetch = FetchType.LAZY)
    public Member member;

    @Builder
    public CulturalEventSearchKeyword(String keyword) {
        this.keyword = keyword;
        this.isRecent = true;
    }

    public void setMember(Member member) {
        if (this.member != null) {
            if (this.member.getCulturalEventSearchKeywordList().contains(this)) {
                this.member.getCulturalEventSearchKeywordList().remove(this);
            }
        }
        this.member = Optional.ofNullable(member).orElse(this.member);
        this.member.getCulturalEventSearchKeywordList().add(this);
    }
}
