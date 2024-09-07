package com.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.demo.domain.GoCamping;
import com.demo.dto.CampingItem;
import com.demo.persistence.GoCampingRepository;

import jakarta.annotation.PostConstruct;

@Service
public class GoCampingService {

    @Autowired
    private GoCampingRepository goCampingRepository;

    @Autowired
    private GoCampingAPI goCampingAPI;
    
//    최초 1회 적용
//    @PostConstruct
//    public void insertInitial() {
//    	importDataFromAPI();
//    }

    public void importDataFromAPI() {
        try {
            List<CampingItem> campingItems = goCampingAPI.getAllCampingSites();
            List<GoCamping> goCampingList = convertToGoCamping(campingItems);
            goCampingRepository.saveAll(goCampingList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<GoCamping> convertToGoCamping(List<CampingItem> campingItems) {
        return campingItems.stream().map(item -> {
            GoCamping goCamping = new GoCamping();
            try {
                goCamping.setContentId(Integer.parseInt(item.getContentId()));
            } catch (NumberFormatException e) {
                // contentId가 정수로 변환되지 않는 경우 기본값 설정 (예: -1)
                goCamping.setContentId(-1);
            }
            goCamping.setFacltNm(sanitize(item.getFacltNm()));
            goCamping.setManageSttus(sanitize(item.getManageSttus()));
            goCamping.setInduty(sanitize(item.getInduty()));
            goCamping.setLctCl(sanitize(item.getLctCl()));
            goCamping.setDoNm(sanitize(item.getDoNm()));
            goCamping.setSigunguNm(sanitize(item.getSigunguNm()));
            goCamping.setOperPdCl(sanitize(item.getOperPdCl()));
            goCamping.setSbrsCl(sanitize(item.getSbrsCl()));
            goCamping.setFacltDivNm(sanitize(item.getFacltDivNm()));
            goCamping.setSiteBottomCl1(sanitize(item.getSiteBottomCl1()));
            goCamping.setSiteBottomCl2(sanitize(item.getSiteBottomCl2()));
            goCamping.setSiteBottomCl3(sanitize(item.getSiteBottomCl3()));
            goCamping.setSiteBottomCl4(sanitize(item.getSiteBottomCl4()));
            goCamping.setSiteBottomCl5(sanitize(item.getSiteBottomCl5()));
            goCamping.setIntro(sanitize(item.getIntro()));
            goCamping.setFirstImageUrl(sanitize(item.getFirstImageUrl()));
            goCamping.setAddr1(sanitize(item.getAddr1()));
            goCamping.setTel(sanitize(item.getTel()));
            goCamping.setHomepage(sanitize(item.getHomepage()));
            goCamping.setResveUrl(sanitize(item.getResveUrl()));
            goCamping.setResveCl(sanitize(item.getResveCl()));
            goCamping.setThemaEnvrnCl(sanitize(item.getThemaEnvrnCl()));
            goCamping.setEqpmnLendCl(sanitize(item.getEqpmnLendCl()));
            goCamping.setRoomCount(sanitize(item.getRoomCount()));
            return goCamping;
        }).collect(Collectors.toList());
    }

    private String sanitize(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("'", "''");
    }

    @Scheduled(cron = "0 0 */2 * * ?")  // 매 2시간마다 실행
    public void scheduleDataImport() {
        importDataFromAPI();
    }
}
