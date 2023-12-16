package project.backend.domain.suspend.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.backend.domain.common.entity.BaseEntity;
import project.backend.domain.member.entity.Member;
import project.backend.domain.suspend.dto.SuspendPatchRequestDto;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Suspend extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "suspend_id")
    public Long id;

    @Column(name = "suspend_deadline")
    public LocalDate suspendDeadline;

    @Column(name = "suspend_reason")
    public String suspendReason;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Suspend(LocalDate suspendDeadline, String suspendReason, Member member) {
        this.suspendDeadline = suspendDeadline;
        this.suspendReason = suspendReason;
        this.member = member;
    }

    // Patch
    public Suspend patchSuspend(LocalDate suspendDeadline, String suspendReason) {
        this.suspendDeadline = Optional.ofNullable(suspendDeadline).orElse(this.suspendDeadline);
        this.suspendReason = Optional.ofNullable(suspendReason).orElse(this.suspendReason);
        return this;
    }
}
