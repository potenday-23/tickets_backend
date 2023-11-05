package project.backend.domain.ticket.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.backend.domain.memberTicketLike.entity.MemberTicketLike;
import project.backend.domain.category.entity.Category;
import project.backend.domain.common.entity.BaseEntity;
import project.backend.domain.ticket.dto.TicketPatchRequestDto;
import project.backend.domain.member.entity.Member;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Ticket extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // todo : IDENTITY와 AUTO 차이점이 뭔지?
    @Column(name = "ticketId")
    public Long id;

    @Column(name = "imageUrl")
    public String imageUrl;

    @Column(name = "ticketImageUrl")
    public String ticketImageUrl;

    @Column(name = "ticketDate")
    public LocalDateTime ticketDate;

    @Column(name = "rating")
    public Float rating;

    @Column(name = "memo")
    public String memo;

    @Column(name = "seat")
    public String seat;

    @Column(name = "location")
    public String location;

    @Column(name = "price")
    public Integer price;

    @Column(name = "friend")
    public String friend;

    @Column(name = "isPrivate")
    public IsPrivate isPrivate = IsPrivate.PRIVATE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    public Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    public Category category;

    @OneToMany(mappedBy = "ticket", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public List<MemberTicketLike> memberTicketLikes = new ArrayList<>();


    @Builder
    public Ticket(String imageUrl, String ticketImageUrl, LocalDateTime ticketDate, Float rating, String memo, String seat,
                  String location, Integer price, String friend, IsPrivate isPrivate) {
        this.imageUrl = imageUrl;
        this.ticketImageUrl = ticketImageUrl;
        this.ticketDate = ticketDate;
        this.rating = rating;
        this.memo = memo;
        this.seat = seat;
        this.location = location;
        this.price = price;
        this.friend = friend;
        this.isPrivate = isPrivate;
    }

    // Patch
    public Ticket patchTicket(TicketPatchRequestDto ticketPatchRequestDto) {
        this.imageUrl = Optional.ofNullable(ticketPatchRequestDto.getImageUrl()).orElse(this.imageUrl);
        this.ticketImageUrl = Optional.ofNullable(ticketPatchRequestDto.getTicketImageUrl()).orElse(this.ticketImageUrl);
        this.ticketDate = Optional.ofNullable(ticketPatchRequestDto.getTicketDate()).orElse(this.ticketDate);
        this.rating = (rating != 0) ? ticketPatchRequestDto.getRating() : this.rating;
        this.memo = Optional.ofNullable(ticketPatchRequestDto.getMemo()).orElse(this.memo);
        this.seat = Optional.ofNullable(ticketPatchRequestDto.getSeat()).orElse(this.seat);
        this.location = Optional.ofNullable(ticketPatchRequestDto.getLocation()).orElse(this.location);
        this.price = (price != 0) ? ticketPatchRequestDto.getPrice() : this.price;
        this.friend = Optional.ofNullable(ticketPatchRequestDto.getFriend()).orElse(this.friend);
        this.isPrivate = Optional.ofNullable(ticketPatchRequestDto.getIsPrivate()).orElse(this.isPrivate);
        return this;
    }

    // == 연관관계 매핑 == //
    public void setMember(Member member) {
        if (this.member != null) {
            if (this.member.getTickets().contains(this)) {
                this.member.getTickets().remove(this);
            }
        }
        this.member = Optional.ofNullable(member).orElse(this.member);
        this.member.getTickets().add(this);
    }

    public void setCategory(Category category) {
        if (this.category != null) {
            if (this.category.getTickets().contains(this)) {
                this.category.getTickets().remove(this);
            }
        }
        this.category = Optional.ofNullable(category).orElse(this.category);
        this.category.getTickets().add(this);
    }
}
