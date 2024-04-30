package project.backend.domain.culturalevent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import project.backend.domain.culturalevnetcategory.entity.CategoryTitle;
import project.backend.global.config.CustomDateDeserializer;
import project.backend.global.config.CustomDateTimeDeserializer;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CulturalEventCreateDto {

    @JsonProperty("goodsName")
    private String title;

    @JsonProperty("goodsLargeImageUrl")
    private String thumbnailImageUrl;

    @JsonProperty("playStartDate")
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private Date startDate;

    @JsonProperty("playEndDate")
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private Date endDate;

    @JsonProperty("ticketOpenDate")
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private LocalDateTime ticketOpenDate;

    @JsonProperty("runningTime")
    private String runningTime;

    @JsonProperty("summary")
    private String summary;

    @JsonProperty("genreSubName")
    private String genre;

    @JsonProperty("contentHtml")
    private String information;

    @JsonProperty("viewRateName")
    private String viewRateName;

    @JsonProperty("placeCode")
    private String placeCode;

    private CategoryTitle categoryTitle;
}
