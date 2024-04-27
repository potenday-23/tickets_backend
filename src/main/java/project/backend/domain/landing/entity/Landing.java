package project.backend.domain.landing.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.backend.domain.common.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Landing extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String email;


    @Builder
    public Landing(String email){
        this.email = email;
    }
}
