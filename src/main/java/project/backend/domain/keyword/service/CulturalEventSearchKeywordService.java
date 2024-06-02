package project.backend.domain.keyword.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.domain.keyword.entity.CulturalEventSearchKeyword;
import project.backend.domain.keyword.repository.CulturalEventSearchKeywordRepository;
import project.backend.domain.member.entity.Member;
import project.backend.domain.member.service.MemberJwtService;
import project.backend.global.error.exception.BusinessException;
import project.backend.global.error.exception.ErrorCode;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CulturalEventSearchKeywordService {

    private final CulturalEventSearchKeywordRepository culturalEventSearchKeywordRepository;
    private final MemberJwtService memberJwtService;


    /**
     * 사용자 검색어 등록
     *
     * @param member
     * @param keyword
     */
    public void createCulturalEventSearchKeyword(Member member, String keyword) {
        Optional<CulturalEventSearchKeyword> culturalEventSearchKeywordOptional = culturalEventSearchKeywordRepository.findFirstByMemberAndKeywordAndIsRecentTrue(member, keyword);

        if (culturalEventSearchKeywordOptional.isEmpty()) {
            CulturalEventSearchKeyword culturalEventSearchKeyword = CulturalEventSearchKeyword.builder()
                    .keyword(keyword).build();
            culturalEventSearchKeyword.setMember(member);
            culturalEventSearchKeywordRepository.save(culturalEventSearchKeyword);

            // 저장 후 키워드가 10개 초과한다면 11번째 키워드 이후를 isRecent false로 변경하기
            List<CulturalEventSearchKeyword> keywordList = member.getCulturalEventSearchKeywordList()
                    .stream()
                    .filter(searchKeyword -> searchKeyword.isRecent)
                    .sorted(Comparator.comparing(CulturalEventSearchKeyword::getUpdatedDate).reversed())
                    .collect(Collectors.toList());

            if (keywordList.size() > 10) {
                keywordList.subList(10, keywordList.size()).forEach(searchKeyword -> {
                    searchKeyword.isRecent = false;
                    culturalEventSearchKeywordRepository.save(searchKeyword);
                });
            }
        } else {
            CulturalEventSearchKeyword culturalEventSearchKeyword = culturalEventSearchKeywordOptional.get();
            culturalEventSearchKeyword.setUpdatedDate(LocalDateTime.now());
            culturalEventSearchKeywordRepository.save(culturalEventSearchKeyword);
        }
    }

    /**
     * 사용자별 최근 검색어 리스트
     *
     * @param member
     * @return List
     */
    public List<CulturalEventSearchKeyword> getCulturalEventRecentKeywordList(Member member) {
        return culturalEventSearchKeywordRepository.findByIsRecentTrueAndMemberOrderByUpdatedDateDesc(member);
    }

    /**
     * 최근 검색어 삭제
     *
     * @param id
     */
    public void deleteCulturalEventSearchKeyword(Long id) {
        // 내가 검색한 키워드인지 확인
        Member member = memberJwtService.getMember();
        CulturalEventSearchKeyword culturalEventSearchKeyword = verifiedCulturalEventSearchKeyword(id);

        if (member != culturalEventSearchKeyword.getMember()) {
            throw new BusinessException(ErrorCode.CULTURAL_EVENT_SEARCH_KEYWORD_DELETE_FAIL);
        }
        culturalEventSearchKeyword.isRecent = false;
    }


    // 인기 키워드 생성(1시간 지난 키워드(is_deleted 중에서) 삭제)
    // TODO : 아직 개발하지 않았음.

    // 키워드 검증
    private CulturalEventSearchKeyword verifiedCulturalEventSearchKeyword(Long id) {
        return culturalEventSearchKeywordRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.NOTICE_NOT_FOUND));
    }
}
