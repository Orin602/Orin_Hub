package com.demo.persistence;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo.domain.MemberData;
import com.demo.domain.WeightRecord;

public interface WeightRecordRepository extends JpaRepository<WeightRecord, Long> {

	// 회원별 체중변화 리스트 호출
	List<WeightRecord> findByMember(MemberData member);

	// 최근 수정한 일주일의 데이터 반환
	@Query("SELECT wr FROM WeightRecord wr JOIN wr.member m WHERE m.id =:id"
			+ " AND wr.re_date >=:startDate ORDER BY wr.re_date DESC")
	public List<WeightRecord> getRecord7ById(@Param("id") String id, @Param("startDate") Date startDate);

	// 최근 수정한 한달의 데이터 반환
	@Query("SELECT wr FROM WeightRecord wr JOIN wr.member m WHERE m.id =:id"
			+ " AND wr.re_date >=:startDate ORDER BY wr.re_date DESC")
	public List<WeightRecord> getRecord30ById(@Param("id") String id, @Param("startDate") Date startDate);

	// 체중 기록을 특정 기간 동안 필터링
	List<WeightRecord> findByMemberIdAndCreatedAtBetween(String id, Date startDate, Date endDate);
}
