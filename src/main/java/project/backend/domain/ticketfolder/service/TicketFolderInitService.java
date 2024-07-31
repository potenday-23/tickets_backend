package project.backend.domain.ticketfolder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import project.backend.domain.member.entity.Member;
import project.backend.domain.member.repository.MemberRepository;
import project.backend.domain.ticketfolder.entity.TicketFolder;
import project.backend.domain.ticketfolder.repository.TicketFolderRepository;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Component
public class TicketFolderInitService implements ApplicationRunner {

    private final TicketFolderRepository ticketFolderRepository;
    private final MemberRepository memberRepository;
    List<String> defaultTicketFolderTitles = Arrays.asList("뮤지컬", "영화", "연극", "콘서트", "전시회");

    // TODO : 최초 실행 후 삭제해야하는 class
    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (Member member : memberRepository.findAllWithTicketFolders()) {
            if (member.getTicketFolderList().isEmpty()) {
                for (String title : defaultTicketFolderTitles) {
                    TicketFolder ticketFolder = TicketFolder.builder()
                            .isDefault(true)
                            .title(title)
                            .member(member)
                            .build();
                    ticketFolderRepository.save(ticketFolder);
                }
            }
        }
    }
}