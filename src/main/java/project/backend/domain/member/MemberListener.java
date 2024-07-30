package project.backend.domain.member;

import org.springframework.stereotype.Component;
import project.backend.domain.member.entity.Member;
import project.backend.domain.ticketfolder.entity.TicketFolder;
import project.backend.domain.ticketfolder.repository.TicketFolderRepository;
import project.backend.global.config.SpringContext;

import javax.persistence.PostPersist;
import java.util.Arrays;
import java.util.List;


@Component
public class MemberListener {

    List<String> defaultTicketFolderTitles = Arrays.asList("뮤지컬", "영화", "연극", "콘서트", "전시회");
    @PostPersist
    public void postPersist(Member member) {
        TicketFolderRepository ticketFolderRepository = SpringContext.getBean(TicketFolderRepository.class);
        for (String title : defaultTicketFolderTitles) {
            TicketFolder ticketFolder = TicketFolder.builder().isDefault(true).title(title).build();
            ticketFolderRepository.save(ticketFolder);
            ticketFolder.setMember(member);
        }

    }
}