package com.demo.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name = "item")
public class BookRecommendation {

    private String drCodeName;
    private String recomtitle;
    private String recomauthor;
    private String recomcontens;
    private String regdate;
    private String recomfilepath;

    @XmlElement(name = "drCodeName")
    public String getDrCodeName() {
        return drCodeName;
    }

    public void setDrCodeName(String drCodeName) {
        this.drCodeName = drCodeName;
    }

    @XmlElement(name = "recomtitle")
    public String getRecomtitle() {
        return recomtitle;
    }

    public void setRecomtitle(String recomtitle) {
        this.recomtitle = recomtitle;
    }

    @XmlElement(name = "recomauthor")
    public String getRecomauthor() {
        return recomauthor;
    }

    public void setRecomauthor(String recomauthor) {
        this.recomauthor = recomauthor;
    }

    @XmlElement(name = "recomcontens")
    public String getRecomcontens() {
        return recomcontens;
    }

    public void setRecomcontens(String recomcontens) {
        this.recomcontens = recomcontens;
    }

    @XmlElement(name = "regdate")
    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    @XmlElement(name = "recomfilepath")
    public String getRecomfilepath() {
        return recomfilepath;
    }

    public void setRecomfilepath(String recomfilepath) {
        this.recomfilepath = recomfilepath;
    }
}
