package com.demo.domain;

import java.util.Date;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
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
@Builder
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class Review {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reviewseq")
    @SequenceGenerator(name = "reviewseq", sequenceName = "reviewseq", allocationSize = 1)	
	private int review_seq; //글번호 고유
	
	@ManyToOne
	@JoinColumn(name = "id")
	private Member member;
	
	private String title;	// 제목
	private String content;	// 내용
    private int favoriteCount;  // 즐겨찾기 수
    private int recommendCount;  // 추천 수
    private int viewCount;  // 조회 수

    @Temporal(value=TemporalType.TIMESTAMP)
    @ColumnDefault("sysdate")
	private Date write_date; //작성날짜
}
