package project.backend.domain.ticket.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.backend.domain.culturalevent.entity.CulturalEvent;
import project.backend.domain.member.entity.Member;
import project.backend.domain.ticket.dto.TicketCreateDto;
import project.backend.domain.ticket.entity.Ticket;
import project.backend.domain.ticket.mapper.TicketMapper;
import project.backend.domain.ticket.service.TicketService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Api(tags = "Ticket - 티켓")
public class TicketController {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;

    @ApiOperation(value = "티켓 생성")
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody TicketCreateDto ticketCreateDto) {
        Ticket ticket = ticketService.createTicket(ticketCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketMapper.ticketToTicketRetrieveDto(ticket));
    }


    @ApiOperation(value = "티켓 객체 조회")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity retrieve(@Positive @PathVariable Long id) {
        Ticket ticket = ticketService.getTicket(id);
        return ResponseEntity.status(HttpStatus.OK).body(ticketMapper.ticketToTicketRetrieveDto(ticket));
    }

    @ApiOperation(value = "티켓 정보 추출(ocr)")
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/extract-info", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity extractInfo(
            @RequestPart(value = "file") MultipartFile file
    ) {
        ResponseEntity<String> response = ticketService.extractInfoFromImage(file);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
}