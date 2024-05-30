package project.backend.domain.member.entity;

import lombok.Getter;

@Getter
public enum Gender {
    MALE("남성"),
    FEMALE("여성");

    private final String status;

    Gender(String status) {
        this.status = status;
    }

}
