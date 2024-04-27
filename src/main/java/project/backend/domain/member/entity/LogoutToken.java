package project.backend.domain.member.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LogoutToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String token;

    @Builder
    public LogoutToken(String token) {
        this.token = token;
    }
}
