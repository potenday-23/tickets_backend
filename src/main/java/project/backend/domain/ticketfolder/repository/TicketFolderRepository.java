package project.backend.domain.ticketfolder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.backend.domain.member.entity.Member;
import project.backend.domain.ticketfolder.entity.TicketFolder;

import java.util.List;


public interface TicketFolderRepository extends JpaRepository<TicketFolder, Long> {
    List<TicketFolder> findAllByMember(Member member);
}
