package project.backend.domain.keyword.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.backend.domain.keyword.entity.CulturalEventSearchKeyword;
import project.backend.domain.member.entity.Member;

import java.util.List;

public interface CulturalEventSearchKeywordRepository extends JpaRepository<CulturalEventSearchKeyword, Long> {

    List<CulturalEventSearchKeyword> findCulturalEventSearchKeywordsByIsDeletedFalseAndMember(Member member);
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM CulturalEventSearchKeyword c WHERE c.isDeleted = false AND c.member = :member AND c.keyword = :keyword")
    Boolean existsByIsDeletedFalseAndMemberAndKeyword(@Param("member") Member member, @Param("keyword") String keyword);
}
