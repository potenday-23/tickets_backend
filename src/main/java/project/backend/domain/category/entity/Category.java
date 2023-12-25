package project.backend.domain.category.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.backend.domain.category.dto.CategoryPatchRequestDto;
import project.backend.domain.common.entity.BaseEntity;
import project.backend.domain.memberTicketLike.entity.MemberTicketLike;
import project.backend.domain.onboardingmembercategory.entity.OnboardingMemberCategory;
import project.backend.domain.ticket.entity.Ticket;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    public Long id;

    @Column(name = "name", unique = true)
    public String name;

    @Column(name = "num")
    public Integer num;

    @Column(name = "basic_image")
    public String basicImage;

    @Column(name = "click_image")
    public String clickImage;

    @OneToMany(mappedBy = "category", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public List<Ticket> tickets = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public List<OnboardingMemberCategory> onboardingMemberCategories = new ArrayList<>();

    // == 생성자 == //
    @Builder
    public Category(String name, Integer num, String basicImage, String clickImage) {
        this.name = name;
        this.num = num;
        this.basicImage = basicImage;
        this.clickImage = clickImage;
    }

    // == 수정 == //
    public Category patchCategory(CategoryPatchRequestDto categoryPatchRequestDto) {
        this.name = Optional.ofNullable(categoryPatchRequestDto.getName()).orElse(this.name);
        this.num = Optional.ofNullable(categoryPatchRequestDto.getNum()).orElse(this.num);
        this.basicImage = Optional.ofNullable(categoryPatchRequestDto.getBasicImage()).orElse(this.basicImage);
        this.clickImage = Optional.ofNullable(categoryPatchRequestDto.getClickImage()).orElse(this.clickImage);
        return this;
    }

}
