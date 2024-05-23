package project.backend.domain.member.entity;

import lombok.Getter;

@Getter
public enum GENDER {
    MALE("남성"),
    FEMALE("여성");

    private final String status;

    GENDER(String status) {
        this.status = status;
    }

}
