package project.backend.domain.traffic.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.backend.domain.common.entity.BaseEntity;
import project.backend.domain.member.entity.Member;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Traffic extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String buttonName;

    @ManyToOne(fetch = FetchType.LAZY)
    public Member member;

    @Builder
    public Traffic(String buttonName) {
        this.buttonName = buttonName;
    }


    // == 연관관계 매핑 == //
    public void setMember(Member member) {
        if (this.member != null) {
            if (this.member.getTraffics().contains(this)) {
                this.member.getTraffics().remove(this);
            }
        }
        this.member = Optional.ofNullable(member).orElse(this.member);
        this.member.getTraffics().add(this);
    }
}
