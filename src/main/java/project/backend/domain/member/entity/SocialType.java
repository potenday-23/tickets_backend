package project.backend.domain.member.entity;

import lombok.Getter;

@Getter
public enum SocialType {
    KAKAO("kakao"),
    APPLE("apple"),
    GOOGLE("google");

    private final String status;

    SocialType(String status) {
        this.status = status;
    }

}
