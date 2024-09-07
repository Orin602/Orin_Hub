package com.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class UltraSrtFcstResponse {
    @JsonProperty("response")
    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public static class Response {
        @JsonProperty("body")
        private Body body;

        public Body getBody() {
            return body;
        }

        public void setBody(Body body) {
            this.body = body;
        }
    }

    public static class Body {
        @JsonProperty("items")
        private Items items;

        public Items getItems() {
            return items;
        }

        public void setItems(Items items) {
            this.items = items;
        }
    }

    public static class Items {
        @JsonProperty("item")
        private List<Item> item;

        public List<Item> getItem() {
            return item;
        }

        public void setItem(List<Item> item) {
            this.item = item;
        }
    }

    public static class Item {
        @JsonProperty("category")
        private String category;

        @JsonProperty("fcstValue")
        private String fcstValue;

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getFcstValue() {
            return fcstValue;
        }

        public void setFcstValue(String fcstValue) {
            this.fcstValue = fcstValue;
        }
    }
}
