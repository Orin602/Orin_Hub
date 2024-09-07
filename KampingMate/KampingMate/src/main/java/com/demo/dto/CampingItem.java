package com.demo.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "CampingItem")
public class CampingItem {

	private String facltNm;
	private String contentId;
    private String manageSttus;
    private String induty;
    private String lctCl;
    private String doNm;
    private String sigunguNm;
    private String operPdCl;
    private String sbrsCl;
    private String roomCount;
    private String facltDivNm;
    private String siteBottomCl1;
    private String siteBottomCl2;
    private String siteBottomCl3;
    private String siteBottomCl4;
    private String siteBottomCl5;
    private String intro;
	private String firstImageUrl;
	private String addr1;
	private String tel;
	private String homepage;
	private String resveUrl;
	private String resveCl;
	private String themaEnvrnCl;
	private String eqpmnLendCl;
	public String getFacltNm() {
		return facltNm;
	}
	public void setFacltNm(String facltNm) {
		this.facltNm = facltNm;
	}
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public String getManageSttus() {
		return manageSttus;
	}
	public void setManageSttus(String manageSttus) {
		this.manageSttus = manageSttus;
	}
	public String getInduty() {
		return induty;
	}
	public void setInduty(String induty) {
		this.induty = induty;
	}
	public String getLctCl() {
		return lctCl;
	}
	public void setLctCl(String lctCl) {
		this.lctCl = lctCl;
	}
	public String getDoNm() {
		return doNm;
	}
	public void setDoNm(String doNm) {
		this.doNm = doNm;
	}
	public String getSigunguNm() {
		return sigunguNm;
	}
	public void setSigunguNm(String sigunguNm) {
		this.sigunguNm = sigunguNm;
	}
	public String getOperPdCl() {
		return operPdCl;
	}
	public void setOperPdCl(String operPdCl) {
		this.operPdCl = operPdCl;
	}
	public String getSbrsCl() {
		return sbrsCl;
	}
	public void setSbrsCl(String sbrsCl) {
		this.sbrsCl = sbrsCl;
	}
	public String getRoomCount() {
		return roomCount;
	}
	public void setRoomCount(String roomCount) {
		this.roomCount = roomCount;
	}
	public String getFacltDivNm() {
		return facltDivNm;
	}
	public void setFacltDivNm(String facltDivNm) {
		this.facltDivNm = facltDivNm;
	}
	public String getSiteBottomCl1() {
		return siteBottomCl1;
	}
	public void setSiteBottomCl1(String siteBottomCl1) {
		this.siteBottomCl1 = siteBottomCl1;
	}
	public String getSiteBottomCl2() {
		return siteBottomCl2;
	}
	public void setSiteBottomCl2(String siteBottomCl2) {
		this.siteBottomCl2 = siteBottomCl2;
	}
	public String getSiteBottomCl3() {
		return siteBottomCl3;
	}
	public void setSiteBottomCl3(String siteBottomCl3) {
		this.siteBottomCl3 = siteBottomCl3;
	}
	public String getSiteBottomCl4() {
		return siteBottomCl4;
	}
	public void setSiteBottomCl4(String siteBottomCl4) {
		this.siteBottomCl4 = siteBottomCl4;
	}
	public String getSiteBottomCl5() {
		return siteBottomCl5;
	}
	public void setSiteBottomCl5(String siteBottomCl5) {
		this.siteBottomCl5 = siteBottomCl5;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getFirstImageUrl() {
		return firstImageUrl;
	}
	public void setFirstImageUrl(String firstImageUrl) {
		this.firstImageUrl = firstImageUrl;
	}
	public String getAddr1() {
		return addr1;
	}
	public void setAddr1(String addr1) {
		this.addr1 = addr1;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getHomepage() {
		return homepage;
	}
	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}
	public String getResveUrl() {
		return resveUrl;
	}
	public void setResveUrl(String resveUrl) {
		this.resveUrl = resveUrl;
	}
	public String getResveCl() {
		return resveCl;
	}
	public void setResveCl(String resveCl) {
		this.resveCl = resveCl;
	}
	public String getThemaEnvrnCl() {
		return themaEnvrnCl;
	}
	public void setThemaEnvrnCl(String themaEnvrnCl) {
		this.themaEnvrnCl = themaEnvrnCl;
	}
	public String getEqpmnLendCl() {
		return eqpmnLendCl;
	}
	public void setEqpmnLendCl(String eqpmnLendCl) {
		this.eqpmnLendCl = eqpmnLendCl;
	}
	public CampingItem(String facltNm, String contentId, String manageSttus, String induty, String lctCl, String doNm,
			String sigunguNm, String operPdCl, String sbrsCl, String facltDivNm, String siteBottomCl1,
			String siteBottomCl2, String siteBottomCl3, String siteBottomCl4, String siteBottomCl5, String intro,
			String firstImageUrl, String addr1, String tel, String homepage, String resveUrl, String resveCl,
			String themaEnvrnCl, String eqpmnLendCl, String roomCount) {
		super();
		this.facltNm = facltNm;
		this.contentId = contentId;
		this.manageSttus = manageSttus;
		this.induty = induty;
		this.lctCl = lctCl;
		this.doNm = doNm;
		this.sigunguNm = sigunguNm;
		this.operPdCl = operPdCl;
		this.sbrsCl = sbrsCl;
		this.roomCount = roomCount;
		this.facltDivNm = facltDivNm;
		this.siteBottomCl1 = siteBottomCl1;
		this.siteBottomCl2 = siteBottomCl2;
		this.siteBottomCl3 = siteBottomCl3;
		this.siteBottomCl4 = siteBottomCl4;
		this.siteBottomCl5 = siteBottomCl5;
		this.intro = intro;
		this.firstImageUrl = firstImageUrl;
		this.addr1 = addr1;
		this.tel = tel;
		this.homepage = homepage;
		this.resveUrl = resveUrl;
		this.resveCl = resveCl;
		this.themaEnvrnCl = themaEnvrnCl;
		this.eqpmnLendCl = eqpmnLendCl;
	}
    
	
	
	
	
	
    
	
	
	
    
    
}

   