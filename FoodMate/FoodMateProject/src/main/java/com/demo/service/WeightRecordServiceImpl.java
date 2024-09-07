package com.demo.service;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.domain.MemberData;
import com.demo.domain.WeightRecord;
import com.demo.persistence.WeightRecordRepository;

@Service
public class WeightRecordServiceImpl implements WeightRecordService {

	@Autowired
	private WeightRecordRepository weightRecordRepo;

	@Override
    public void saveWeightRecord(WeightRecord weightRecord) {
        weightRecordRepo.save(weightRecord);
    }
	
	// 회원별 체중 기록 조회
	@Override
    public List<WeightRecord> getWeightRecordsByMemberId(String id) {
        // 회원 ID를 기준으로 해당 회원의 체중 기록을 가져옵니다.
        MemberData member = new MemberData();
        member.setId(id);
        return weightRecordRepo.findByMember(member);
    }

	// 최근 7일의 체중 기록을 반환하는 메서드
	@Override
	public List<WeightRecord> getRecentWeekRecords(String id) {
		// 현재 날짜를 기준으로 7일 전의 날짜를 계산
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        Date startDate = cal.getTime();

     // 최근 7일의 기록을 가져옵니다.
	    List<WeightRecord> records = weightRecordRepo.getRecord7ById(id, startDate);

	    // 날짜를 키로 가지는 LinkedHashMap을 생성하여 중복된 날짜를 하나로.
	    // 중복된 날짜는 WeightRecord의 re_date가 같은 날짜중 updatedAt가 가장 늦은 시간의 값을 기준으로 함
	    Map<Date, WeightRecord> dateMap = new LinkedHashMap<>();
        for (WeightRecord record : records) {
            if (!dateMap.containsKey(record.getRe_date()) || record.getUpdatedAt().after(dateMap.get(record.getRe_date()).getUpdatedAt())) {
                dateMap.put(record.getRe_date(), record);
            }
        }

	    // 최대 7개의 데이터만 선택합니다.
	    List<WeightRecord> result = dateMap.values().stream().limit(7).collect(Collectors.toList());

	    return result;
	}

	// 최근 30일의 체중 기록을 반환하는 메서드
	@Override
	public List<WeightRecord> getRecentMonthRecords(String id) {
		// 현재 날짜를 기준으로 30일 전의 날짜를 계산
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -30);
        Date startDate = cal.getTime();

        // 최근 30일의 기록을 가져옵니다.
	    List<WeightRecord> records = weightRecordRepo.getRecord30ById(id, startDate);

	    // 날짜를 키로 가지는 LinkedHashMap을 생성하여 중복된 날짜를 하나로 합칩니다.
	    // 중복된 날짜는 WeightRecord의 re_date가 같은 날짜중 updatedAt가 가장 늦은 시간의 값을 기준으로 함
	    Map<Date, WeightRecord> dateMap = new LinkedHashMap<>();
        for (WeightRecord record : records) {
            if (!dateMap.containsKey(record.getRe_date()) || record.getUpdatedAt().after(dateMap.get(record.getRe_date()).getUpdatedAt())) {
                dateMap.put(record.getRe_date(), record);
            } 
        }
        
        // 최대 30개의 데이터만 선택합니다.
	    List<WeightRecord> result = dateMap.values().stream().limit(30).collect(Collectors.toList());

	    return result;
	}
	

	// 주간/월간 변화 평균값 계산 메서드
	@Override
	public double calculateAverageWeight(List<WeightRecord> records) {
		if(records.isEmpty()) {
			return 0.0;
		}
		double sum = 0.0;
        for (WeightRecord record : records) {
            sum += record.getRe_weight();
        }
        return sum / records.size();
	}

	@Override
	public List<WeightRecord> getWeirghtRecordFiltering(String id, Date startDate, Date endDate) {
		return weightRecordRepo.findByMemberIdAndCreatedAtBetween(id, startDate, endDate);
	}
	
	
}
