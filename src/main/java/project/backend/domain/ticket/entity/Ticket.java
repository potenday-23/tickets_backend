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

import javax.persistence.*;
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
    public Long id;

    public String title;

    public String imageUrl;

    public LocalDateTime ticketDate;

    public Float rating;

    public String memo;

    public String seat;

    public String location;

    public Integer price;

    public String friend;
    public String color;

    public String ticketType;
    public String layoutType;

    @Enumerated(EnumType.STRING)
    public IsPrivate isPrivate = IsPrivate.PRIVATE;

    @ManyToOne(fetch = FetchType.LAZY)
    public Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    public Category category;

    @OneToMany(mappedBy = "ticket", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public List<MemberTicketLike> memberTicketLikes = new ArrayList<>();


    @Builder
    public Ticket(String title, String imageUrl, LocalDateTime ticketDate, Float rating, String memo, String seat,
                  String location, Integer price, String friend, String color, IsPrivate isPrivate, String ticketType, String layoutType) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.ticketDate = ticketDate;
        this.rating = rating;
        this.memo = memo;
        this.seat = seat;
        this.location = location;
        this.price = price;
        this.friend = friend;
        this.color = color;
        this.isPrivate = isPrivate;
        this.ticketType = ticketType;
        this.layoutType = layoutType;
    }

    // Patch
    public Ticket patchTicket(TicketPatchRequestDto ticketPatchRequestDto) {
        this.title = Optional.ofNullable(ticketPatchRequestDto.getTitle()).orElse(this.title);
        this.imageUrl = Optional.ofNullable(ticketPatchRequestDto.getImageUrl()).orElse(this.imageUrl);
        this.ticketDate = Optional.ofNullable(ticketPatchRequestDto.getTicketLocalDateTime()).orElse(this.ticketDate);
        this.rating = (ticketPatchRequestDto.getRating()!= null && ticketPatchRequestDto.getRating() != 0) ? ticketPatchRequestDto.getRating() : this.rating;
        this.memo = Optional.ofNullable(ticketPatchRequestDto.getMemo()).orElse(this.memo);
        this.seat = Optional.ofNullable(ticketPatchRequestDto.getSeat()).orElse(this.seat);
        this.location = Optional.ofNullable(ticketPatchRequestDto.getLocation()).orElse(this.location);
        this.price = (ticketPatchRequestDto.getPrice() != null && ticketPatchRequestDto.getPrice() != 0) ? ticketPatchRequestDto.getPrice() : this.price;
        this.friend = Optional.ofNullable(ticketPatchRequestDto.getFriend()).orElse(this.friend);
        this.isPrivate = Optional.ofNullable(ticketPatchRequestDto.getIsPrivate()).orElse(this.isPrivate);
        this.color = Optional.ofNullable(ticketPatchRequestDto.getColor()).orElse(this.color);
        this.ticketType = Optional.ofNullable(ticketPatchRequestDto.getTicketType()).orElse(this.ticketType);
        this.layoutType = Optional.ofNullable(ticketPatchRequestDto.getLayoutType()).orElse(this.layoutType);
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
