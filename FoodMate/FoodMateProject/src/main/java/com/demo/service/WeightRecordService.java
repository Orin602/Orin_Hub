package com.demo.service;

import java.util.Date;
import java.util.List;

import com.demo.domain.WeightRecord;

public interface WeightRecordService {

	// 체중 기록 저장
    void saveWeightRecord(WeightRecord weightRecord);

    // 회원별 체중 기록 조회
	public List<WeightRecord> getWeightRecordsByMemberId(String id);
	
	// 최근 7일의 체중 기록을 반환하는 메서드
    List<WeightRecord> getRecentWeekRecords(String id);

    // 최근 30일의 체중 기록을 반환하는 메서드
    List<WeightRecord> getRecentMonthRecords(String id);
    
    // 주간/월간 변화 평균값 계산 메서드
    double calculateAverageWeight(List<WeightRecord> records);
    
    // 특정 기간 동안의 체중기록 필터링
    public List<WeightRecord> getWeirghtRecordFiltering(String id, Date startDate, Date endDate);
}
