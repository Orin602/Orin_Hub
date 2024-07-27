package com.demo.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name = "list")
public class BookRecommendationWrapper {

    private List<BookRecommendation> items = new ArrayList<>();

    @XmlElement(name = "item")
    public List<BookRecommendation> getItems() {
        return items;
    }

    public void setItems(List<BookRecommendation> items) {
        this.items = items;
    }
}
