package com.demo.dto;

import java.util.List;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name = "channel")
public class BookRecommendationResponse {

    private int totalCount;
    private List<BookRecommendation> list;

    @XmlElement(name = "totalCount")
    public int getTotalCount() {
        return totalCount;
    }

    @XmlElementWrapper(name = "list")
    @XmlElement(name = "item")
    public List<BookRecommendation> getList() {
        return list;
    }
}
