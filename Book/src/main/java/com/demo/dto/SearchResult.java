package com.demo.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchResult {

	private List<ItemDTO> items;
    private int pageNum;
    private int totalPages;
    private int totalResults;

    public SearchResult(List<ItemDTO> items, int pageNum, int totalPages, int totalResults) {
        this.items = items;
        this.pageNum = pageNum;
        this.totalPages = totalPages;
        this.totalResults = totalResults;
    }
}
