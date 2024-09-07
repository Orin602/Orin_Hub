package com.demo.dto;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NShopping {
	private String title;
	private String link;
	private String image;
	private long lprice; //최저가
	private long productId; //id
	private String category1;
	private String category2;
	private String category3;
	private String category4;
	private String maker; //제조사
	private String brand; //브랜드
	private int productType;
	private Date postdate;
}