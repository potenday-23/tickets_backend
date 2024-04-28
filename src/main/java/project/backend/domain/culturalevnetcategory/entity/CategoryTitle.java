package project.backend.domain.culturalevnetcategory.entity;

import lombok.Getter;

@Getter
public enum CategoryTitle {
    DRAMA("DRAMA", "연극"),
    MUSICAL("MUSICAL", "뮤지컬"),
    CONCERT("CONCERT", "콘서트"),
    EXHIBIT("EXHIBIT", "전시회"),
    MOVIE("MOVIE", "영화");

    private final String titleChoice;
    private final String title;

    CategoryTitle(String titleChoice, String title) {
        this.titleChoice = titleChoice;
        this.title = title;
    }

}
