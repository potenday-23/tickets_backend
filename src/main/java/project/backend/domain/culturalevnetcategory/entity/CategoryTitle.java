package project.backend.domain.culturalevnetcategory.entity;

import lombok.Getter;

@Getter
public enum CategoryTitle {
    // 메인 카테고리
    ALL("ALL", "전체", 1),

    // 문화 생활 카테고리
    MUSICAL("MUSICAL", "뮤지컬", 2),
    DRAMA("DRAMA", "연극", 3),
    CONCERT("CONCERT", "콘서트", 4),
    EXHIBIT("EXHIBIT", "전시회", 5),
    MOVIE("MOVIE", "영화", 6);

    private final String type;
    private final String name;
    private final Integer ordering;

    CategoryTitle(String type, String name, Integer ordering) {
        this.type = type;
        this.name = name;
        this.ordering = ordering;
    }
}
