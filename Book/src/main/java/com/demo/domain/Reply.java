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
public class Reply {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int replySeq; // 고유번호 시퀀스 자동생성 및 1씩 증가
	
	@ManyToOne
	@JoinColumn(name="id")
	private Member member;
	
	@ManyToOne
	@JoinColumn(name="review_seq")
	private Review review;
	
	@Temporal(value=TemporalType.TIMESTAMP)
	@ColumnDefault("sysdate")
	private Date reply_date; // 게시글작성 시간 저장(sysdate)
	
	private int likes; // 댓글의 좋아요 수
	private String content;	// 댓글 내용
}
