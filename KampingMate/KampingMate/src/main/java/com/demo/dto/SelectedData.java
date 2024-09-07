package com.demo.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SelectedData {
    private List<String> doNm;
    private List<String> gungu;
    private List<String> faclt;
    private List<String> lct;
    private List<String> induty;
    private List<String> bottom;
    private List<String> sbrs;
    private int page;
    private String keyword;
}
