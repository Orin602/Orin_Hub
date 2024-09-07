package com.demo.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


import com.demo.domain.MemberData;
import com.demo.domain.askBoard;

public interface MemberDataRepository extends JpaRepository<MemberData, Long > {
	//구현때문에 만들어둔것입니다 덮어씌워주세용

	// 마이페이지
	Optional<MemberData> findById(askBoard askBoard);
    
	/**
	* MemberData 수정
	* 키, 몸무게, bmi(자동 계산), age, gender, goal
	* Member의 id와 같은 MemberData의 정보 수정
	*/
	@Transactional
	@Modifying
	@Query("UPDATE MemberData md SET md.age = :age WHERE md.id = :id")
	void updateMemberData(@Param("id") String id, @Param("age") Integer age);

	
	// 채팅서비스
    Optional<MemberData> findById(long no_data);
}
