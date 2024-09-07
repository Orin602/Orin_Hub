package com.demo.domain;

import java.util.Date;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class Recommend_History {

	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY) // 문자열 타입인 경우 IDENTITY가 아닌 AUTO 사용
	    private int historyId;
	
	 	@ManyToOne
	    @JoinColumn(name = "idx", nullable = false) // 외래 키로 수정
	    private foodRecipe recommendFood;
	
	 	@ManyToOne
	    @JoinColumn(name = "no_data", nullable = false) // 외래 키로 수정
	    private MemberData memberData;
	 
	 	@Temporal(TemporalType.TIMESTAMP)
	 	@ColumnDefault("sysdate")
	    private Date recommendDate; // Date 대신 LocalDateTime 사용
	 	
	 	// idx와 user_id를 조합하여 유니크한 키 생성
	    @Column(nullable = false, unique = true)
	    private String idxAndMemberId;


	    @PrePersist
	    private void generateIdxAndMemberId() {
	        this.idxAndMemberId = generateIdxAndMemberId(this.recommendFood.getIdx(), this.memberData.getId());
	    }

	    private static String generateIdxAndMemberId(long idx, String memberid) {
	        // idx와 memberId를 조합하여 해시값 생성
	        return String.valueOf(idx) + "_" + String.valueOf(memberid);
	    }

}