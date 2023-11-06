package project.backend.domain.ticket.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import project.backend.domain.member.dto.MemberStatisticsResponseDto;
import project.backend.domain.member.entity.Member;
import project.backend.domain.member.entity.QMember;
import project.backend.domain.ticket.entity.IsPrivate;
import project.backend.domain.ticket.entity.Ticket;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static project.backend.domain.ticket.entity.QTicket.ticket;
import static project.backend.domain.member.entity.QMember.member;

@RequiredArgsConstructor
public class TicketRepositoryImpl implements TicketRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Ticket> getTotalTicketList(List<String> categorys, List<LocalDateTime> startAndEndList, String search) {
        if (categorys == null || categorys.size() == 0) {
            return queryFactory.selectFrom(ticket)
                    .where(ticket.isPrivate.eq(IsPrivate.PUBLIC),
                            ticket.title.contains(search),
                            ticket.ticketDate.between(startAndEndList.get(0), startAndEndList.get(1))
                    )
                    .orderBy(ticket.ticketDate.desc())
                    .fetch();
        }

        else {
            return queryFactory.selectFrom(ticket)
                    .where(ticket.isPrivate.eq(IsPrivate.PUBLIC),
                            ticket.category.name.in(categorys),
                            ticket.title.contains(search),
                            ticket.ticketDate.between(startAndEndList.get(0), startAndEndList.get(1))
                    )
                    .orderBy(ticket.ticketDate.desc())
                    .fetch();
        }
    }

    @Override
    public List<Ticket> getMyTicketList(List<String> categorys, List<LocalDateTime> startAndEndList, String search, Member member) {
        if (categorys == null || categorys.size() == 0) {
            return queryFactory.selectFrom(ticket)
                    .where(ticket.title.contains(search),
                            ticket.ticketDate.between(startAndEndList.get(0), startAndEndList.get(1)),
                            ticket.member.eq(member)
                    )
                    .orderBy(ticket.ticketDate.desc())
                    .fetch();
        }

        else {
            return queryFactory.selectFrom(ticket)
                    .where(ticket.category.name.in(categorys),
                            ticket.title.contains(search),
                            ticket.ticketDate.between(startAndEndList.get(0), startAndEndList.get(1)),
                            ticket.member.eq(member)
                    )
                    .orderBy(ticket.ticketDate.desc())
                    .fetch();
        }
    }

    @Override
    public List<MemberStatisticsResponseDto> getStatisticsList(Member member) {

        List<MemberStatisticsResponseDto> memberStatisticsResponseDtoList = new ArrayList<>();

        List<Tuple> categoryCountList = queryFactory
                .select(ticket.category.name, ticket.count())
                .from(ticket)
                .groupBy(ticket.category)
                .where(ticket.member.eq(member))
                .fetch();

        for(Tuple categoryCount : categoryCountList) {
            memberStatisticsResponseDtoList.add(MemberStatisticsResponseDto.builder()
                    .category(categoryCount.get(0, String.class))
                    .categoryCnt(categoryCount.get(1, Long.class))
                    .build());
        }
        return memberStatisticsResponseDtoList;
    }
}
