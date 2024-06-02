package project.backend.domain.keyword.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.domain.keyword.dto.CulturalEventPopularKeywordListDto;
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
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional
public class CulturalEventSearchKeywordService {

    private final CulturalEventSearchKeywordRepository culturalEventSearchKeywordRepository;
    private final MemberJwtService memberJwtService;
    private List<CulturalEventPopularKeywordListDto> cachedPopularKeywords;


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

    /**
     * 시간별 인기 검색어 리스트
     *
     * @return List
     */
    public List<CulturalEventPopularKeywordListDto> getCulturalEventPopularKeywordList() {
        // Variables
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        PageRequest pageRequest = PageRequest.of(0, 7);

        // Get Keywords
        List<Object[]> topKeywords = culturalEventSearchKeywordRepository.findTopKeywordsWithinOneHour(oneHourAgo, pageRequest);

        // Make Dto List
        return IntStream.range(0, topKeywords.size())
                .mapToObj(i -> CulturalEventPopularKeywordListDto.builder().ordering(i + 1).keyword((String) topKeywords.get(i)[0]).build())
                .collect(Collectors.toList());
    }


    /**
     * 정각마다 실행
     */
    @Scheduled(cron = "0 0 * * * *")  // 매 정각마다 실행
    public void cachePopularKeywords() {
        cachedPopularKeywords = getCulturalEventPopularKeywordList();
    }

    /**
     * 캐시된 인기 검색어 리스트
     * @return List
     */
    public List<CulturalEventPopularKeywordListDto> getCachedPopularKeywords() {
        if (cachedPopularKeywords == null || cachedPopularKeywords.isEmpty()) {
            cachePopularKeywords();
        }
        return cachedPopularKeywords;
    }

    /**
     * 키워드 객체 검증
     * @param id
     * @return CulturalEventSearchKeyword
     */
    private CulturalEventSearchKeyword verifiedCulturalEventSearchKeyword(Long id) {
        return culturalEventSearchKeywordRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.NOTICE_NOT_FOUND));
    }
}
