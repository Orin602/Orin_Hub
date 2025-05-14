package com.demo.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name = "channel")
public class BookRecommendationResponse {

    private int totalCount;
    private List<BookRecommendationWrapper> lists = new ArrayList<>();

    @XmlElement(name = "totalCount")
    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    @XmlElement(name = "list")
    public List<BookRecommendationWrapper> getLists() {
        return lists;
    }

    public void setLists(List<BookRecommendationWrapper> lists) {
        this.lists = lists;
    }
}
