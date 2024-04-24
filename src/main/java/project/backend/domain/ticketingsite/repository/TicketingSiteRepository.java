package project.backend.domain.ticketingsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.backend.domain.ticketingsite.entity.TicketingSite;

public interface TicketingSiteRepository extends JpaRepository<TicketingSite, Long> {
}
