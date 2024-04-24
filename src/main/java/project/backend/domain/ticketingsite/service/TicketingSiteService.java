package project.backend.domain.ticketingsite.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.domain.ticketingsite.dto.TicketingSitePostRequestDto;
import project.backend.domain.ticketingsite.entity.TicketingSite;
import project.backend.domain.ticketingsite.mapper.TicketingSiteMapper;
import project.backend.domain.ticketingsite.repository.TicketingSiteRepository;
import project.backend.global.error.exception.BusinessException;
import project.backend.global.error.exception.ErrorCode;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketingSiteService {
    private final TicketingSiteRepository ticketingSiteRepository;
    private final TicketingSiteMapper ticketingSiteMapper;

    public TicketingSite createTicketingSite(TicketingSitePostRequestDto ticketingSitePostRequestDto) {
        TicketingSite ticketingSite = TicketingSite.builder()
                .platform(ticketingSitePostRequestDto.getPlatform())
                .link(ticketingSitePostRequestDto.getLink()).build();
        ticketingSiteRepository.save(ticketingSite);
        return ticketingSite;
    }

    public TicketingSite getTicketingSite(Long id) {
        return verifiedTicketingSite(id);
    }

    public List<TicketingSite> getTicketingSiteList() {
        return ticketingSiteRepository.findAll();
    }

    public void deleteTicketingSite(Long id) {
        ticketingSiteRepository.delete(verifiedTicketingSite(id));
    }

    private TicketingSite verifiedTicketingSite(Long id) {
        return ticketingSiteRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.NOTICE_NOT_FOUND));
    }

}
