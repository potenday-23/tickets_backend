package project.backend.domain.media.entity;

import lombok.*;
import project.backend.domain.common.entity.BaseEntity;
import project.backend.domain.place.entity.Place;
import project.backend.domain.ticket.entity.Ticket;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Getter
@Setter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Media extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String mediaUrl;

    public Integer ordering;

    public Boolean isThumbnail;

    @ManyToOne(fetch = FetchType.LAZY)
    public Ticket ticket;

    @Builder
    public Media(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public void setTicket(Ticket ticket) {
        if (this.ticket != null) {
            if (this.ticket.getMedias().contains(this)) {
                this.ticket.getMedias().remove(this);
            }
        }
        this.ticket = Optional.ofNullable(ticket).orElse(this.ticket);
        this.ticket.getMedias().add(this);
    }
}
