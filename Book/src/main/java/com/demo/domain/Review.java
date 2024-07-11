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
@ToString
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class Review {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reviewseq")
	@SequenceGenerator(name = "reviewseq", sequenceName = "reviewseq", allocationSize = 1)
	private int review_seq;
	
	private String title;
	private String content;
	private int viewCount; // 조회수
	private int recoCount; // 추천수
	private int checkCount; // 즐겨찾기수
	private String uploadFilePath; // 업로드 경로
	
	@ManyToOne
	@JoinColumn(name="id")
	private Member member;	// member테이블 조회
	
	@Temporal(value=TemporalType.TIMESTAMP)
	@ColumnDefault("sysdate")
	private Date review_date; // 게시글작성 시간 저장(sysdate)
}
