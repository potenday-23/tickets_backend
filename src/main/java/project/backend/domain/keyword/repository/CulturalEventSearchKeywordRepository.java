package project.backend.domain.keyword.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.backend.domain.keyword.entity.CulturalEventSearchKeyword;
import project.backend.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface CulturalEventSearchKeywordRepository extends JpaRepository<CulturalEventSearchKeyword, Long> {

    List<CulturalEventSearchKeyword> findByIsRecentTrueAndMemberOrderByUpdatedDateDesc(Member member);

    Optional<CulturalEventSearchKeyword> findFirstByMemberAndKeywordAndIsRecentTrue(Member member, String keyword);
}
