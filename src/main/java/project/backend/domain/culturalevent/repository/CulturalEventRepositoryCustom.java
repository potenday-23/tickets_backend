package project.backend.domain.culturalevent.repository;

import project.backend.domain.culturalevent.entity.CulturalEvent;
import project.backend.domain.culturalevnetcategory.entity.CategoryTitle;
import project.backend.domain.member.dto.MemberStatisticsResponseDto;
import project.backend.domain.member.dto.MemberYearStatisticsResponseDto;
import project.backend.domain.member.entity.Member;
import project.backend.domain.ticket.entity.Ticket;

import java.time.LocalDateTime;
import java.util.List;

public interface CulturalEventRepositoryCustom {
    List<CulturalEvent> getCulturalEventList(int page, int size, CategoryTitle category, String ordering, Boolean isOpened);
}
