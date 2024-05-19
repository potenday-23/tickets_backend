package project.backend.domain.ticketingsite.entity;

import lombok.Getter;

@Getter
public enum Platform {
    INTERPARK("인터파크", "https://tickets2323.s3.ap-northeast-2.amazonaws.com/Images/interpark.png"),
    YES24("예스 24", "https://tickets2323.s3.ap-northeast-2.amazonaws.com/Images/yes24.png"),
    MELON("멜론", "https://tickets2323.s3.ap-northeast-2.amazonaws.com/Images/melon.png");

    private final String name;
    private final String imageUrl;

    Platform(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }
}
