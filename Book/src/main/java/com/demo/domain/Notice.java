package com.demo.domain;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.ElementCollection;
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
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Builder
@ToString
@Entity
public class Notice {	//공지사항 table

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "noticeseq")
	@SequenceGenerator(name="noticeseq", sequenceName="noticeseq", allocationSize = 1)
	private int notice_seq;	//공지사항 seq
	
	private String title;	//공지사항 제목
	private String content;	//공지사항 내용
	
	@ColumnDefault("0")
	private int likeCount;	//공지사항 좋아요수
	@ColumnDefault("0")
	private int viewCount;	//공지사항 조회수
	
	private String noticeImageUrl;	//공지사항 이미지 url
	@ElementCollection
	private List<String> uploadedImages;
	
	
	@Temporal(value=TemporalType.TIMESTAMP)
	@ColumnDefault("sysdate")
	private Date notice_date;	//공지사항 작성 날짜
	
	// 
	@ManyToOne
	@JoinColumn(name="id")
	private Member member;	//Member테이블 조회
	
}
