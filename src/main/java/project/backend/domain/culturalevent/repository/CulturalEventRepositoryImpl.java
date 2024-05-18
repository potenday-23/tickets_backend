package project.backend.domain.culturalevent.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import project.backend.domain.culturalevent.entity.CulturalEvent;
import project.backend.domain.culturalevnetcategory.entity.CategoryTitle;

import java.time.LocalDateTime;
import java.util.List;


import static project.backend.domain.culturalevent.entity.QCulturalEvent.culturalEvent;

@RequiredArgsConstructor
public class CulturalEventRepositoryImpl implements CulturalEventRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<CulturalEvent> getCulturalEventList(int page, int size, CategoryTitle category, String ordering, Boolean isOpened) {
        LocalDateTime now = LocalDateTime.now();
        JPAQuery<CulturalEvent> culturalEventJPAQuery = queryFactory.selectFrom(culturalEvent);

        // ordering 있을 경우
        if (ordering != null) {
            if (ordering.equals("-point")) {
                culturalEventJPAQuery.orderBy(culturalEvent.point.desc());
            } if (ordering.equals("ticketOpenDate")) {
                culturalEventJPAQuery.orderBy(culturalEvent.ticketOpenDate.asc());
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
        if (!(category == CategoryTitle.ALL || category == null)) {
            culturalEventJPAQuery.where(culturalEvent.culturalEventCategory.title.eq(category));
        }

        // page, size 적용
        Pageable pageable = PageRequest.of(page, size);
        culturalEventJPAQuery.offset(pageable.getOffset());
        culturalEventJPAQuery.limit(pageable.getPageSize());

        // Result
        return culturalEventJPAQuery.fetch();
    }
}
