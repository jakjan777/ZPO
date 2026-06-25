package com.project.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RestResponsePage<T> extends PageImpl<T> {
    private static final long serialVersionUID = 1L;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public RestResponsePage(@JsonProperty("content") List<T> content,
            @JsonProperty("number") int number,
            @JsonProperty("size") int size,
            @JsonProperty("totalElements") Long totalElements,
            @JsonProperty("pageable") JsonNode pageable,
            @JsonProperty("page") JsonNode page,
            @JsonProperty("last") boolean last,
            @JsonProperty("totalPages") int totalPages,
            @JsonProperty("sort") JsonNode sort,
            @JsonProperty("first") boolean first,
            @JsonProperty("numberOfElements") int numberOfElements) {
        super(getContent(content),
                PageRequest.of(getPageNumber(number, page), getPageSize(size, page, content)),
                getTotalElements(totalElements, page, content));
    }

    public RestResponsePage(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public RestResponsePage(List<T> content) {
        super(content);
    }

    public RestResponsePage() {
        super(new ArrayList<>());
    }

    private static <T> List<T> getContent(List<T> content) {
        return content != null ? content : new ArrayList<>();
    }

    private static int getPageNumber(int number, JsonNode page) {
        if (page != null && page.has("number")) {
            return page.get("number").asInt();
        }
        return number;
    }

    private static <T> int getPageSize(int size, JsonNode page, List<T> content) {
        if (size > 0) {
            return size;
        }
        if (page != null && page.has("size") && page.get("size").asInt() > 0) {
            return page.get("size").asInt();
        }
        int contentSize = getContent(content).size();
        return contentSize > 0 ? contentSize : 1;
    }

    private static <T> long getTotalElements(Long totalElements, JsonNode page, List<T> content) {
        if (totalElements != null) {
            return totalElements;
        }
        if (page != null && page.has("totalElements")) {
            return page.get("totalElements").asLong();
        }
        return getContent(content).size();
    }
}
