package project.backend.domain.keyword.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.domain.keyword.entity.CulturalEventSearchKeyword;
import project.backend.domain.keyword.repository.CulturalEventSearchKeywordRepository;
import project.backend.domain.member.entity.Member;
import project.backend.domain.quit.entity.Quit;
import project.backend.domain.ticket.entity.Ticket;
import project.backend.global.error.exception.BusinessException;
import project.backend.global.error.exception.ErrorCode;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CulturalEventSearchKeywordService {

    private final CulturalEventSearchKeywordRepository culturalEventSearchKeywordRepository;

    // 사용자, 키워드 등록
    public void createCulturalEventSearchKeyword(Member member, String keyword) {
        if (!culturalEventSearchKeywordRepository.existsByIsRecentFalseAndMemberAndKeyword(member, keyword)) {
            CulturalEventSearchKeyword culturalEventSearchKeyword = CulturalEventSearchKeyword.builder()
                    .keyword(keyword).build();

            culturalEventSearchKeyword.setMember(member);
            culturalEventSearchKeywordRepository.save(culturalEventSearchKeyword);
        }
    }

    // 키워드 삭제
    public void deleteCulturalEventSearchKeyword(Long id) {
        CulturalEventSearchKeyword culturalEventSearchKeyword = verifiedCulturalEventSearchKeyword(id);
        culturalEventSearchKeyword.isRecent = true;
    }

    // 사용자 키워드 목록 조회
    public List<CulturalEventSearchKeyword> getCulturalEventSearchKeywordList(Member member) {
        return culturalEventSearchKeywordRepository.findCulturalEventSearchKeywordsByIsRecentFalseAndMember(member);
    }


    // 인기 키워드 생성(1시간 지난 키워드(is_deleted 중에서) 삭제)
    // TODO : 아직 개발하지 않았음.

    // 키워드 검증
    private CulturalEventSearchKeyword verifiedCulturalEventSearchKeyword(Long id) {
        return culturalEventSearchKeywordRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.NOTICE_NOT_FOUND));
    }
}
