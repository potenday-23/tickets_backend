package project.backend.domain.common.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDate;

@MappedSuperclass // todo : 이거 왜 쓰는가
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class) // todo : 이건 또 왜 쓰는가
public abstract class BaseEntity { // todo : abstract 에 대해서 좀 더 알아보기

    @CreatedDate
    @Column(name = "created_date", updatable = false) // todo : 이건 머임
    private LocalDate createdDate;

    @LastModifiedDate
    @Column(name = "updated_date", updatable = false)
    private LocalDate updatedDate; //todo : 왜 private 인 것일까?

}
