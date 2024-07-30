package project.backend.domain.ticketfolder.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.backend.domain.ticket.dto.TicketCreateDto;
import project.backend.domain.ticket.entity.Ticket;
import project.backend.domain.ticketfolder.dto.TicketFolderCreateDto;
import project.backend.domain.ticketfolder.dto.TicketFolderRetrieveDto;
import project.backend.domain.ticketfolder.entity.TicketFolder;
import project.backend.domain.ticketfolder.mapper.TicketFolderMapper;
import project.backend.domain.ticketfolder.service.TicketFolderService;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "TicketFolder - 티켓 폴더")
@RestController
@RequestMapping("/api/ticket-folders")
@RequiredArgsConstructor
public class TicketFolderController {
    private final TicketFolderService ticketFolderService;
    private final TicketFolderMapper ticketFolderMapper;

    @ApiOperation(value = "티켓 폴더 리스트 조회")
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity list() {
        List<TicketFolder> ticketFolderList = ticketFolderService.getTicketFolderList();
        List<TicketFolderRetrieveDto> ticketFolderRetrieveDtoList = ticketFolderMapper.ticketFolderToTicketFolderRetrieveDto(ticketFolderList);
        return ResponseEntity.status(HttpStatus.OK).body(ticketFolderRetrieveDtoList);
    }

    @ApiOperation(value = "티켓 폴더 생성")
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody TicketFolderCreateDto ticketFolderCreateDto) {
        TicketFolder ticketFolder = ticketFolderService.createTicketFolder(ticketFolderCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketFolderMapper.ticketFolderToTicketFolderRetrieveDto(ticketFolder));
    }
}
