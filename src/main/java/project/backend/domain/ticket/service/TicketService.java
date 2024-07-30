package project.backend.domain.ticket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
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

import java.io.IOException;

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
            media = mediaService.setMediaOrdering(mediaDto);
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

    public ResponseEntity<String> extractInfoFromImage(MultipartFile file) {
        try {
            // Prepare headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // Create MultiValueMap to hold the file
            LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", getByteArrayResource(file));

            // Create an HttpEntity with the file and headers
            HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Build the URI for the OCR API
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl("http://13.125.32.85:8000/api/ocr");

            // Create RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();

            // Send request to OCR API and get the response
            ResponseEntity<String> response = restTemplate.exchange(
                    uriBuilder.toUriString(),
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            // Return the response from OCR API to the client
            return response;
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.NOTICE_NOT_FOUND);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.NOTICE_NOT_FOUND);
        }
    }

    private Resource getByteArrayResource(MultipartFile file) throws IOException {
        return new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };
    }
}
