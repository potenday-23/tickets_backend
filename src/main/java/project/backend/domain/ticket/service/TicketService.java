package project.backend.domain.ticket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.domain.media.dto.MediaDto;
import project.backend.domain.media.entity.Media;
import project.backend.domain.media.service.MediaService;
import project.backend.domain.member.entity.Member;
import project.backend.domain.member.service.MemberJwtService;
import project.backend.domain.ticket.dto.TicketCreateDto;
import project.backend.domain.ticket.entity.Ticket;
import project.backend.domain.ticket.repository.TicketRepository;
import project.backend.global.error.exception.BusinessException;
import project.backend.global.error.exception.ErrorCode;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketService {
    private final TicketRepository ticketRepository;
    private final MediaService mediaService;
    private final MemberJwtService memberJwtService;

    public Ticket createTicket(TicketCreateDto ticketCreateDto) {
        Media media;
        Ticket ticket = Ticket.builder()
                .title(ticketCreateDto.getTitle())
                .mainImageUrl(ticketCreateDto.getMainImageUrl())
                .date(ticketCreateDto.getDate())
                .score(ticketCreateDto.getScore())
                .review(ticketCreateDto.getReview())
                .seat(ticketCreateDto.getSeat())
                .price(ticketCreateDto.getPrice())
                .build();

        // Media 연결
        for (MediaDto mediaDto : ticketCreateDto.medias) {
            media = mediaService.setOrderThumbnail(mediaDto);
            media.setTicket(ticket);
        }
        ticket.setMember(memberJwtService.getMember());
        ticketRepository.save(ticket);

        return ticket;
    }

    public Ticket getTicket(Long id) {
        Member member = memberJwtService.getMember();
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.TICKET_NOT_FOUND));
        if (ticket.member != member) {
            throw new BusinessException(ErrorCode.TICKET_VIEW_FAIL);
        }
        return ticket;
    }

    public Ticket verifiedTicket(Long id) {
        return ticketRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.TICKET_NOT_FOUND));
    }
}
