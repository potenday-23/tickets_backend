package project.backend.domain.culturalevent.entity;

import lombok.Getter;

@Getter
public enum CulturalEventStatus {
    PERFORMING("공연중"),
    SCHEDULED("공연예정"),
    END("공연마감");

    private final String status;

    CulturalEventStatus(String status) {
        this.status = status;
    }
}
