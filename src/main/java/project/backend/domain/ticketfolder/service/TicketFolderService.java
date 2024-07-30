package project.backend.domain.ticketfolder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.domain.category.dto.CategoryResponseDto;
import project.backend.domain.member.entity.Member;
import project.backend.domain.member.service.MemberJwtService;
import project.backend.domain.ticketfolder.dto.TicketFolderCreateDto;
import project.backend.domain.ticketfolder.dto.TicketFolderRetrieveDto;
import project.backend.domain.ticketfolder.entity.TicketFolder;
import project.backend.domain.ticketfolder.mapper.TicketFolderMapper;
import project.backend.domain.ticketfolder.repository.TicketFolderRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketFolderService {
    private final MemberJwtService memberJwtService;
    private final TicketFolderRepository ticketFolderRepository;

    public TicketFolder createTicketFolder(TicketFolderCreateDto ticketFolderCreateDto) {
        Member member = memberJwtService.getMember();
        TicketFolder ticketFolder = TicketFolder.builder().title(ticketFolderCreateDto.getTitle()).member(member).build();
        return ticketFolderRepository.save(ticketFolder);
    }

    public List<TicketFolder> getTicketFolderList() {
        Member member = memberJwtService.getMember();
        return ticketFolderRepository.findAllByMember(member);
    }
}
