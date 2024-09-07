package com.demo.domain;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
public class GoCamping {
	@Id
	private int contentId;
	private String facltNm;
	private String manageSttus;
	private String induty;
	private String lctCl;	
	private String doNm;
	private String sigunguNm;
	private String operPdCl;
	private String sbrsCl;
	private String facltDivNm;
	private String siteBottomCl1;
	private String siteBottomCl2;
	private String siteBottomCl3;
	private String siteBottomCl4;
	private String siteBottomCl5;
	
	@Column(columnDefinition = "CLOB")
	private String intro;
	@Column(columnDefinition = "VARCHAR2(2000)")
	private String firstImageUrl;
	private String addr1;
	private String tel;
	@Column(columnDefinition = "VARCHAR2(2000)")
	private String homepage;
	@Column(columnDefinition = "VARCHAR2(2000)")
	private String resveUrl;
	private String resveCl;
	private String themaEnvrnCl;
	private String eqpmnLendCl;
	private String roomCount;
	
	

}
