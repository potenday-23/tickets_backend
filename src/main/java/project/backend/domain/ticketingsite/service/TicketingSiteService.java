package project.backend.domain.ticketingsite.service;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.domain.ticketingsite.dto.TicketingSitePostRequestDto;
import project.backend.domain.ticketingsite.entity.Platform;
import project.backend.domain.ticketingsite.entity.TicketingSite;
import project.backend.domain.ticketingsite.mapper.TicketingSiteMapper;
import project.backend.domain.ticketingsite.repository.TicketingSiteRepository;
import project.backend.global.error.exception.BusinessException;
import project.backend.global.error.exception.ErrorCode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketingSiteService {
    private final TicketingSiteRepository ticketingSiteRepository;
    private final TicketingSiteMapper ticketingSiteMapper;

    public List<TicketingSite> createTicketingSite(String goodsCode, String title) {

        List<TicketingSite> ticketingSiteList = new ArrayList<>();

        // Interpark url
        String baseUrl = "https://tickets.interpark.com/goods/";
        ticketingSiteList.add(
                TicketingSite.builder()
                        .platform(Platform.INTERPARK)
                        .link(baseUrl + goodsCode).build()
        );

        // Yes24 url
        String yes24Link = getYes24Link(title);
        if (yes24Link != null) {
            ticketingSiteList.add(
                    TicketingSite.builder()
                            .platform(Platform.YES24)
                            .link(yes24Link).build()
            );
        }

        // Melon url
        String melonLink = getMelonLink(title);
        if (melonLink != null) {
            ticketingSiteList.add(
                    TicketingSite.builder()
                            .platform(Platform.MELON)
                            .link(melonLink).build()
            );
        }

        ticketingSiteRepository.saveAll(ticketingSiteList);
        return ticketingSiteList;
    }

    public String getYes24Link(String culturalEventName) {
        String searchUrl = "http://ticket.yes24.com/New/Search/Ajax/axPerfList.aspx";

        try {
            Document document = Jsoup.connect(searchUrl)
                    .data("searchText", culturalEventName)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .post();

            Element firstListItem = document.selectFirst(".srch-list-item");

            if (firstListItem != null) {
                Element linkElement = firstListItem.selectFirst("a[href^=/Perf/]");

                if (linkElement != null) {
                    String relativeHref = linkElement.attr("href");
                    String fullUrl = "http://ticket.yes24.com" + relativeHref;
                    return fullUrl;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getMelonLink (String culturalEventName) {
        String url = "https://ticket.melon.com/search/ajax/listPerformance.htm?q=" + culturalEventName;

        try {
            Document document = Jsoup.connect(url).get();

            Element firstPerformance = document.selectFirst(".box_movie .tbl .show_infor .thumb_90x125 a");

            if (firstPerformance != null) {
                String href = firstPerformance.attr("href");
                String prodId = extractProdId(href);

                if (prodId != null) {
                    System.out.println("Extracted prodId: " + prodId);
                    return "https://ticket.melon.com/performance/index.htm?prodId=" + prodId;
                } else {
                    System.out.println("prodId not found in the href attribute.");
                    return null;
                }
            } else {
                System.out.println("No performance link found.");
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String extractProdId(String href) {
        int start = href.indexOf("prodId=") + "prodId=".length();
        if (start != -1) {
            int end = href.indexOf("&", start);
            if (end == -1) {
                end = href.length();
            }
            return href.substring(start, end);
        }
        return null;
    }

}
