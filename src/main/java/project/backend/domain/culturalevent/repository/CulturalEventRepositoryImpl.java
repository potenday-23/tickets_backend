package project.backend.domain.culturalevent.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import project.backend.domain.culturalevent.entity.CulturalEvent;
import project.backend.domain.culturalevnetcategory.entity.CategoryTitle;
import project.backend.domain.member.entity.Member;
import project.backend.domain.member.service.MemberJwtService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static project.backend.domain.culturalevent.entity.QCulturalEvent.culturalEvent;
import static project.backend.domain.visit.entity.QCulturalEventVisit.culturalEventVisit;

@RequiredArgsConstructor
public class CulturalEventRepositoryImpl implements CulturalEventRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final MemberJwtService memberJwtService;

    @Override
    public List<CulturalEvent> getCulturalEventList(int page, int size, List<CategoryTitle> categories, String ordering, Boolean isOpened, Double latitude, Double longitude) {
        // 현재 시간
        LocalDateTime now = LocalDateTime.now();
        ZonedDateTime zonedDateTime = now.atZone(ZoneId.systemDefault());
        Date dateNow = Date.from(zonedDateTime.toInstant());

        /// Query 객체
        JPAQuery<CulturalEvent> culturalEventJPAQuery = queryFactory.selectFrom(culturalEvent);

        // 지난 문화생활 제외
        culturalEventJPAQuery.where(culturalEvent.endDate.after(dateNow));

        // ordering 있을 경우
        if (ordering != null) {
            if (ordering.equals("-point")) {
                culturalEventJPAQuery.orderBy(culturalEvent.point.desc());
            } else if (ordering.equals("ticketOpenDate")) {
                culturalEventJPAQuery.orderBy(culturalEvent.ticketOpenDate.asc());
            } else if (ordering.equals("-updatedDate")) {
                culturalEventJPAQuery.orderBy(culturalEvent.updatedDate.desc());
            } else if (ordering.equals("endDate")) {
                culturalEventJPAQuery.orderBy(culturalEvent.endDate.asc());
            } else if (ordering.equals("recommend")) {
                // latitude(위도), longitude(경도) 있을 경우
                if (latitude != null && longitude != null) {
                    double radiusInKm = 50.0;
                    double radiusInDegrees = radiusInKm / 111.0;

                    culturalEventJPAQuery.where(
                            culturalEvent.place.latitude.between(latitude - radiusInDegrees, latitude + radiusInDegrees)
                                    .and(culturalEvent.place.longitude.between(longitude - radiusInDegrees, longitude + radiusInDegrees))
                    );
                }
//                List<Long> recommendOrdering = requestRecommend(latitude, longitude, getMemberCulturalEventList()); // [200L, 201L, 202L, ...]
//                OrderSpecifier<Double> orderSpecifier = createWeightOrderSpecifier(recommendOrdering);
//                culturalEventJPAQuery.orderBy(orderSpecifier);
            }
        }

        // isOpened 있을 경우
        if (isOpened != null) {
            if (isOpened) {
                culturalEventJPAQuery.where(culturalEvent.ticketOpenDate.before(now));
            } else {
                culturalEventJPAQuery.where(culturalEvent.ticketOpenDate.after(now));
            }
        }

        // category 있을 경우
        if (!(categories == null || categories.contains(CategoryTitle.ALL))) {
            culturalEventJPAQuery.where(culturalEvent.culturalEventCategory.title.in(categories));
        }

        // page, size 적용
        Pageable pageable = PageRequest.of(page, size);
        culturalEventJPAQuery.offset(pageable.getOffset());
        culturalEventJPAQuery.limit(pageable.getPageSize());

        // Result
        return culturalEventJPAQuery.fetch();
    }

    @Override
    public List<CulturalEvent> getCulturalEventSearchList(int page, int size, String keyword) {

        // 현재 시간
        LocalDateTime now = LocalDateTime.now();
        ZonedDateTime zonedDateTime = now.atZone(ZoneId.systemDefault());
        Date dateNow = Date.from(zonedDateTime.toInstant());

        // Query 객체
        JPAQuery<CulturalEvent> culturalEventJPAQuery = queryFactory.selectFrom(culturalEvent);

        // 지난 문화생활 제외
        culturalEventJPAQuery.where(culturalEvent.endDate.after(dateNow));

        // keyword 검색
        if (keyword != null && !keyword.isEmpty()) {
            culturalEventJPAQuery.where(culturalEvent.title.contains(keyword));
        }

        // 인기순
        culturalEventJPAQuery.orderBy(culturalEvent.point.desc());

        // page, size 적용
        Pageable pageable = PageRequest.of(page, size);
        culturalEventJPAQuery.offset(pageable.getOffset());
        culturalEventJPAQuery.limit(pageable.getPageSize());

        return culturalEventJPAQuery.fetch();
    }


    @Override
    public List<CulturalEvent> getMemberCulturalEventList() {
        Member member = memberJwtService.getMember();
        JPAQuery<CulturalEvent> culturalEventJPAQuery = queryFactory.selectFrom(culturalEvent);

        if (member == null || member.getCulturalEventVisitList().isEmpty()) {
            // 1. 조회한 문화생활이 없을 경우
            culturalEventJPAQuery.orderBy(culturalEvent.point.desc());
        } else {
            // 2. 조회한 문화생활이 있을 경우
            culturalEventJPAQuery
                    .join(culturalEvent.culturalEventVisitList, culturalEventVisit)
                    .where(culturalEventVisit.member.eq(member))
                    .orderBy(culturalEventVisit.createdDate.desc());
        }

        return culturalEventJPAQuery.limit(3).fetch();
    }

    private OrderSpecifier<Double> createWeightOrderSpecifier(List<Long> hello) {
        NumberTemplate<Double> weight = (NumberTemplate<Double>) new CaseBuilder()
                .when(culturalEvent.id.in(hello))
                .then(Expressions.constant(1.0))
                .otherwise(Expressions.constant(0.0));
        return weight.asc();
    }

    private List<Long> requestRecommend(Double latitude, Double longitude, List<CulturalEvent> culturalEvents) {
        // Example logic to generate recommended IDs, should be replaced with actual implementation
        return Arrays.asList(200L, 201L, 301L);
    }
}
