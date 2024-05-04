package project.backend.domain.culturaleventevalutaion.entity;

import lombok.Getter;

@Getter
public enum EvaluationType {
    EXPECT("기대평"),
    REVIEW("관람평");

    private final String name;

    EvaluationType(String name) {
        this.name = name;
    }
}
