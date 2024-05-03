package project.backend.domain.culturalevnetinfo.service;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.domain.culturalevent.entity.CulturalEvent;
import project.backend.domain.culturalevnetinfo.dto.CulturalEventInfoCreateDto;
import project.backend.domain.culturalevnetinfo.entity.CulturalEventInfo;
import project.backend.domain.culturalevnetinfo.mapper.CulturalEventInfoMapper;
import project.backend.domain.culturalevnetinfo.repository.CulturalEventInfoRepository;
import project.backend.global.error.exception.BusinessException;
import project.backend.global.error.exception.ErrorCode;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CulturalEventInfoService {
    private final CulturalEventInfoRepository culturalEventInfoRepository;
    private final CulturalEventInfoMapper culturalEventInfoMapper;

    public List<String> extractImageUrlList(String culturalEventInfoHtml) {
        List<String> imageUrlList = new ArrayList<>();
        Document doc = Jsoup.parse(culturalEventInfoHtml);
        Elements imgTags = doc.select("img");
        for (Element img : imgTags) {
            String src = img.attr("src");
            imageUrlList.add(src);
        }
        return imageUrlList;
    }

    public List<String> getImageUrlList(CulturalEvent culturalEvent) {
        List<String> imageUrlList = new ArrayList<>();

        for (CulturalEventInfo culturalEventInfo: culturalEvent.getCulturalEvnetInfoList()) {
            imageUrlList.add(culturalEventInfo.getImageUrl());
        }

        return imageUrlList;
    }

    public CulturalEventInfo createCulturalEventInfo(CulturalEventInfoCreateDto culturalEventInfoCreateDto) {
        CulturalEventInfo culturalEventInfo = CulturalEventInfo.builder()
                .imageUrl(culturalEventInfoCreateDto.getImageUrl())
                .text(culturalEventInfoCreateDto.getText())
                .ordering(culturalEventInfoCreateDto.getOrdering()).build();
        culturalEventInfoRepository.save(culturalEventInfo);
        return culturalEventInfo;
    }

    public CulturalEventInfo getCulturalEventInfo(Long id) {
        return verifiedCulturalEventInfo(id);
    }

    public List<CulturalEventInfo> getCulturalEventInfoList() {
        return culturalEventInfoRepository.findAll();
    }

    public void deleteCulturalEventInfo(Long id) {
        culturalEventInfoRepository.delete(verifiedCulturalEventInfo(id));
    }

    private CulturalEventInfo verifiedCulturalEventInfo(Long id) {
        return culturalEventInfoRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.NOTICE_NOT_FOUND));
    }

}
