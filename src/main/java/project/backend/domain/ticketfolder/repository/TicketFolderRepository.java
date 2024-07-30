package project.backend.domain.ticketfolder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.backend.domain.ticketfolder.entity.TicketFolder;


public interface TicketFolderRepository extends JpaRepository<TicketFolder, Long> {
}
