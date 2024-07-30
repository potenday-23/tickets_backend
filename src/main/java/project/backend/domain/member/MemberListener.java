package project.backend.domain.member;

import org.springframework.stereotype.Component;
import project.backend.domain.member.entity.Member;

import javax.persistence.PrePersist;
import java.util.Arrays;
import java.util.List;


@Component
public class MemberListener {
    List<String> defaultTicketFolderTitles = Arrays.asList("뮤지컬", "영화", "연극", "콘서트", "전시회");

    @PrePersist
    public void prePersist(Member member) {
        // 만약, 유저가 새로 생성된다면
        // Ticket Folder 만들기
        // 기본 데이터 : 뮤지컬, 영화, 연극, 콘서트, 전시회
    }
}