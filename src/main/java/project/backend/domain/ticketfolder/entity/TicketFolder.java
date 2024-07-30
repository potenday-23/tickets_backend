package project.backend.domain.ticketfolder.entity;

import lombok.*;
import project.backend.domain.common.entity.BaseEntity;
import project.backend.domain.ticket.entity.Ticket;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Setter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TicketFolder extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public Boolean isDefault;
    public String title;

    @OneToMany(mappedBy = "ticket_folder", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public List<Ticket> tickets = new ArrayList<>();

    @Builder
    public TicketFolder(Boolean isDefault, String title) {
        this.isDefault = isDefault;
        this.title = title;
    }
}
