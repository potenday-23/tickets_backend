package project.backend.domain.keyword.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.backend.domain.keyword.entity.CulturalEventSearchKeyword;
import project.backend.domain.member.entity.Member;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CulturalEventSearchKeywordRepository extends JpaRepository<CulturalEventSearchKeyword, Long> {

    List<CulturalEventSearchKeyword> findByIsRecentTrueAndMemberOrderByUpdatedDateDesc(Member member);

    Optional<CulturalEventSearchKeyword> findFirstByMemberAndKeywordAndIsRecentTrue(Member member, String keyword);

    @Query("SELECT k.keyword, COUNT(k) AS freq FROM CulturalEventSearchKeyword k WHERE k.updatedDate >= :oneHourAgo GROUP BY k.keyword ORDER BY freq DESC")
    List<Object[]> findTopKeywordsWithinOneHour(@Param("oneHourAgo") LocalDateTime oneHourAgo, Pageable pageable);
}
