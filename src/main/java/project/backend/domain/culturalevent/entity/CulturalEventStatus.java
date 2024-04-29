package project.backend.domain.culturalevent.entity;

import lombok.Getter;

@Getter
public enum CulturalEventStatus {
    OPENING("진행중"),
    UPCOMING("진행예정"),
    CLOSED("마감");

    private final String status;

    CulturalEventStatus(String status) {
        this.status = status;
    }
}
