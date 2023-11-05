package project.backend.domain.memberTicketLike.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.backend.domain.ticket.entity.Ticket;
import project.backend.domain.member.entity.Member;

import jakarta.persistence.*;
import java.util.Optional;

@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberTicketLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberTicketLikeId")
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    public Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticketId")
    public Ticket ticket;

    // == 생성자 == //
    @Builder
    public MemberTicketLike(Member member, Ticket ticket) {
        this.member = member;
        this.ticket = ticket;
    }

    // == 연관관계 매핑 == //
    public void setMemberTicket(Member member, Ticket ticket) {
        if (this.member != null) {
            if (this.member.getMemberTicketLikes().contains(this)) {
                this.member.getMemberTicketLikes().remove(this);
            }
        }
        if (this.ticket != null) {
            if (this.ticket.getMemberTicketLikes().contains(this)) {
                this.ticket.getMemberTicketLikes().remove(this);
            }
        }
        this.member = Optional.ofNullable(member).orElse(this.member);
        this.member.getMemberTicketLikes().add(this);
        this.ticket = Optional.ofNullable(ticket).orElse(this.ticket);
        this.ticket.getMemberTicketLikes().add(this);
    }
}
