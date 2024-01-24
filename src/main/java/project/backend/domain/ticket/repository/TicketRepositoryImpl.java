package project.backend.domain.ticket.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.criterion.Projections;
import org.joda.time.DateTime;
import project.backend.domain.member.dto.MemberStatisticsResponseDto;
import project.backend.domain.member.dto.MemberYearStatisticsResponseDto;
import project.backend.domain.member.entity.Member;
import project.backend.domain.member.entity.QMember;
import project.backend.domain.ticket.entity.IsPrivate;
import project.backend.domain.ticket.entity.Ticket;
import project.backend.global.error.exception.BusinessException;
import project.backend.global.error.exception.ErrorCode;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static project.backend.domain.ticket.entity.QTicket.ticket;
import static project.backend.domain.member.entity.QMember.member;

@RequiredArgsConstructor
public class TicketRepositoryImpl implements TicketRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Ticket> getTotalTicketList(List<String> categorys, List<LocalDateTime> startAndEndList, String search) {
        search = search==null ? "": search;
        if (categorys == null || categorys.size() == 0) {
            return queryFactory.selectFrom(ticket)
                    .where(ticket.isPrivate.eq(IsPrivate.PUBLIC),
                            ticket.title.contains(search),
                            ticket.ticketDate.between(startAndEndList.get(0), startAndEndList.get(1))
                    )
                    .orderBy(ticket.createdDate.desc())
                    .fetch();
        }

        else {
            return queryFactory.selectFrom(ticket)
                    .where(ticket.isPrivate.eq(IsPrivate.PUBLIC),
                            ticket.category.name.in(categorys),
                            ticket.title.contains(search),
                            ticket.ticketDate.between(startAndEndList.get(0), startAndEndList.get(1))
                    )
                    .orderBy(ticket.createdDate.desc())
                    .fetch();
        }
    }

    @Override
    public List<Ticket> getTotalAndMyTicketList(List<String> categorys, List<LocalDateTime> startAndEndList, String search, Member member) {
        search = search==null ? "": search;
        if (categorys == null || categorys.size() == 0) {
            return queryFactory.selectFrom(ticket)
                    .where(eqMember(ticket.member.id, member),
                            ticket.title.contains(search),
                            ticket.ticketDate.between(startAndEndList.get(0), startAndEndList.get(1))
                    )
                    .orderBy(ticket.createdDate.desc())
                    .fetch();
        }

        else {
            return queryFactory.selectFrom(ticket)
                    .where(eqMember(ticket.member.id, member),
                            ticket.category.name.in(categorys),
                            ticket.title.contains(search),
                            ticket.ticketDate.between(startAndEndList.get(0), startAndEndList.get(1))
                    )
                    .orderBy(ticket.createdDate.desc())
                    .fetch();
        }
    }

    @Override
    public List<Ticket> getMyTicketList(List<String> categorys, List<LocalDateTime> startAndEndList, String search, Member member) {
        search = search==null ? "": search;
        if (categorys == null || categorys.size() == 0) {
            return queryFactory.selectFrom(ticket)
                    .where(ticket.title.contains(search),
                            ticket.ticketDate.between(startAndEndList.get(0), startAndEndList.get(1)),
                            ticket.member.eq(member)
                    )
                    .orderBy(ticket.createdDate.desc())
                    .fetch();
        }

        else {
            return queryFactory.selectFrom(ticket)
                    .where(ticket.category.name.in(categorys),
                            ticket.title.contains(search),
                            ticket.ticketDate.between(startAndEndList.get(0), startAndEndList.get(1)),
                            ticket.member.eq(member)
                    )
                    .orderBy(ticket.createdDate.desc())
                    .fetch();
        }
    }

    @Override
    public List<MemberStatisticsResponseDto> getStatisticsList(Member member, String month) {

        DecimalFormat df = new DecimalFormat("0.0");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime start = LocalDate.now().minusYears(100).atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);
        if (month != null) {
            try {
                LocalDate searchMonth = LocalDate.parse(month+"-01", dateTimeFormatter);
                start = searchMonth.withDayOfMonth(1).atStartOfDay();
                end = searchMonth.withDayOfMonth(searchMonth.lengthOfMonth()).atTime(LocalTime.MAX);
            } catch (Exception e) {
                throw new BusinessException(ErrorCode.MONTH_FORMAT_BAD_REQUEST);
            }
        }

        List<Tuple> categoryCountList = queryFactory
                .select(ticket.category.name, ticket.count())
                .from(ticket)
                .groupBy(ticket.category)
                .where(ticket.member.eq(member),
                        ticket.ticketDate.between(start, end))
                .orderBy(ticket.count().desc())
                .fetch();

        List<MemberStatisticsResponseDto> memberStatisticsResponseDtoList = new ArrayList<>();

        for(Tuple categoryCount : categoryCountList) {
            memberStatisticsResponseDtoList.add(MemberStatisticsResponseDto.builder()
                    .category(categoryCount.get(0, String.class))
                    .categoryCnt(categoryCount.get(1, Long.class))
                    .build());
        }

        long resultCnt = memberStatisticsResponseDtoList.stream().mapToLong(MemberStatisticsResponseDto::getCategoryCnt).sum();
        for (int i = 0; memberStatisticsResponseDtoList.size() > i; i++) {
            Double percent = (memberStatisticsResponseDtoList.get(i).getCategoryCnt().doubleValue()/resultCnt) * 100;
            memberStatisticsResponseDtoList.get(i).setCategoryPercent(Double.parseDouble(df.format(percent)));
        }
        return memberStatisticsResponseDtoList;
    }

    @Override
    public List<MemberYearStatisticsResponseDto> getYearStatisticsList(Member member) {

        LocalDateTime CURRENT_TIME = LocalDateTime.now();
        List<MemberYearStatisticsResponseDto> memberYearStatisticsResponseDtoList = new ArrayList<>();

        // 월별 GroupBy를 위한 String Template 이용하기
        StringTemplate formattedDate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                ticket.ticketDate,
                ConstantImpl.create("%Y-%m"));

        // 월별 통계가 있는 경우 Tuple[년, 월, 통계수]를 반환
        List<Tuple> ticketCountList = queryFactory.select(ticket.ticketDate.year(),ticket.ticketDate.month(), ticket.count())
                .from(ticket)
                .groupBy(formattedDate)
                .where(
                        ticket.member.eq(member),
                        ticket.ticketDate.between(CURRENT_TIME.minusYears(10).with(TemporalAdjusters.firstDayOfYear()), CURRENT_TIME.with(TemporalAdjusters.lastDayOfYear())))
                .fetch();

        // memberYearStatisticsResponseDtoList에 저장
        for (Tuple ticketCount: ticketCountList) {
            memberYearStatisticsResponseDtoList.add(MemberYearStatisticsResponseDto.builder()
                    .year(ticketCount.get(0, Integer.class))
                    .month(ticketCount.get(1, Integer.class))
                    .count(Long.valueOf(Optional.ofNullable(ticketCount.get(2, Long.class)).orElse(0L)).intValue()).build());
        }

        return memberYearStatisticsResponseDtoList;
    }


    private BooleanExpression eqMember(NumberPath<Long> memberId, Member member){
        NumberPath<Long> memberIdCompare = Expressions.numberPath(Long.class, member.getId().toString());
        if (Objects.equals(memberIdCompare, memberId)) {
            return null;
        }
        return ticket.isPrivate.eq(IsPrivate.PUBLIC);
    }

}
