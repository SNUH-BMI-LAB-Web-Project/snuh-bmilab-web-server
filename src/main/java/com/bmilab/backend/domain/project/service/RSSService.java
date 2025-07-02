package com.bmilab.backend.domain.project.service;

import com.bmilab.backend.domain.project.dto.response.RSSResponse;
import com.bmilab.backend.domain.project.dto.response.RSSResponse.RSSItem;
import com.bmilab.backend.domain.project.dto.response.external.NTISAssignmentResponse;
import com.bmilab.backend.domain.project.dto.response.external.NTISAssignmentResponse.AssignmentItem;
import com.bmilab.backend.domain.project.enums.RssSearchType;
import com.bmilab.backend.domain.project.exception.ProjectErrorCode;
import com.bmilab.backend.global.exception.ApiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class RSSService {

    private static final String NTIS_RSS_URL = "https://www.ntis.go.kr/rndgate/unRndRss.xml";
    private final ObjectMapper objectMapper;

    public NTISAssignmentResponse getAllRecentNTISAssignments() {

        RestClient restClient = RestClient.create(NTIS_RSS_URL);

        String bodyText = restClient.get()
                .uri((uriBuilder) -> uriBuilder.queryParam("prt", 600).queryParam("Fi", 0).build())
                .retrieve()
                .body(String.class);

        JSONObject bodyObject = XML.toJSONObject(Objects.requireNonNull(bodyText));

        JSONArray items = bodyObject.getJSONObject("rss").getJSONObject("channel").getJSONArray("item");

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("items", items);

        try {
            return objectMapper.readValue(jsonObject.toString(), NTISAssignmentResponse.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public RSSResponse getAllRssAssignments(
            int pageNo,
            int size,
            RssSearchType searchType,
            String keyword,
            Long minBudget,
            Long maxBudget
    ) {

        int startIndex = pageNo * size;

        NTISAssignmentResponse ntisResults = getAllRecentNTISAssignments();

        if (ntisResults == null) {
            throw new ApiException(ProjectErrorCode.INVALID_RSS_DATA);
        }

        Stream<AssignmentItem> itemsStream = ntisResults.items().stream();

        if (searchType != null && keyword != null && !keyword.isBlank()) {
            String lowerKeyword = keyword.toLowerCase();
            switch (searchType) {
                case TITLE -> itemsStream = itemsStream.filter(item ->
                        item.title() != null && item.title().toLowerCase().contains(lowerKeyword));
                case ORGANIZATION -> itemsStream = itemsStream.filter(item ->
                        item.author() != null && item.author().toLowerCase().contains(lowerKeyword));
                case DEPARTMENT -> itemsStream = itemsStream.filter(item ->
                        item.category() != null && item.category().toLowerCase().contains(lowerKeyword));
            }
        }

        if (minBudget != null) {
            itemsStream = itemsStream.filter(item -> item.budget() >= minBudget);
        }

        if (maxBudget != null) {
            itemsStream = itemsStream.filter(item -> item.budget() <= maxBudget);
        }

        List<AssignmentItem> filtered = itemsStream.toList();

        int totalCount = filtered.size();

        List<RSSItem> items = filtered.stream().skip(startIndex).limit(size).map(RSSItem::from).toList();

        int totalPage = (int) Math.ceil((double) totalCount / (double) size);

        return new RSSResponse(items, totalPage);
    }
}
