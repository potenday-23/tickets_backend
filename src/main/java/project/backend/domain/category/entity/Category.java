package project.backend.domain.category.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.backend.domain.memberTicketLike.entity.MemberTicketLike;
import project.backend.domain.onboardingmembercategory.entity.OnboardingMemberCategory;
import project.backend.domain.ticket.entity.Ticket;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryId")
    public Long id;

    @Column(name = "name", unique = true)
    public String name;

    @OneToMany(mappedBy = "category", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public List<Ticket> tickets = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public List<OnboardingMemberCategory> onboardingMemberCategories = new ArrayList<>();

    // == 생성자 == //
    @Builder
    public Category(String name) {
        this.name = name;
    }

}
