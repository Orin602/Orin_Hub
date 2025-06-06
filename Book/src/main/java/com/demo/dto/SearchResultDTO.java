package com.demo.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true) // 알 수 없는 속성을 무시
public class SearchResultDTO {

    @JsonProperty("item") // JSON의 'item' 필드를 매핑
    private List<ItemDTO> item; // DTO에서 'item' 필드로 매핑
    
    private List<ItemDTO> items;
    private int totalResults; // 전체 검색 결과 수
    private int totalPages; // 전체 페이지 수

    public SearchResultDTO(List<ItemDTO> items, int totalResults) {
        this.items = items;
        this.totalResults = totalResults;
    }
}
