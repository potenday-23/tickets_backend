package project.backend.domain.ticket.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.backend.domain.category.entity.Category;
import project.backend.domain.common.entity.BaseEntity;
import project.backend.domain.media.entity.Media;
import project.backend.domain.place.entity.Place;
import project.backend.domain.member.entity.Member;
import project.backend.domain.ticketfolder.entity.TicketFolder;
import project.backend.domain.ticketingsite.entity.TicketingSite;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Ticket extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String mainImageUrl;
    public String title;
    public LocalDate date;
    public String seat;
    public Integer price;
    public String review;
    public Float score;

    @ManyToOne(fetch = FetchType.LAZY)
    public Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    public Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    public Place place;

    @ManyToOne(fetch = FetchType.LAZY)
    public TicketFolder ticketFolder;

    @OneToMany(mappedBy = "ticket", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public List<Media> medias = new ArrayList<>();


    @Builder
    public Ticket(String title, String mainImageUrl, LocalDate date, Float score, String review, String seat,
                  Integer price, Member member, Category category, Place place) {
        this.title = title;
        this.mainImageUrl = mainImageUrl;
        this.date = date;
        this.score = score;
        this.review = review;
        this.seat = seat;
        this.price = price;
        this.member = member;
        this.category = category;
        this.place = place;
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

    public void setPlace(Place place) {
        if (this.place != null) {
            if (this.place.getTickets().contains(this)) {
                this.place.getTickets().remove(this);
            }
        }
        this.place = Optional.ofNullable(place).orElse(this.place);
        this.place.getTickets().add(this);
    }

    public void setTicketFolder(TicketFolder ticketFolder) {
        if (this.ticketFolder != null) {
            if (this.ticketFolder.getTickets().contains(this)) {
                this.ticketFolder.getTickets().remove(this);
            }
        }
        this.ticketFolder = Optional.ofNullable(ticketFolder).orElse(this.ticketFolder);
        this.ticketFolder.getTickets().add(this);
    }
}
