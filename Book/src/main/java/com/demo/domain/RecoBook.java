package com.demo.domain;

import java.util.Date;

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

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@ToString
@Builder
public class RecoBook {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long recoBookSeq;
	
	private String title;	// 도서명
	private String author;	// 저자
	private String image;	// 이미지
	private int score;		// 평점
	private int sales_count;	// 책 판매수
	private String genre;	// 해시태그
	
	@ManyToOne
	@JoinColumn(name = "member_seq")
	private Member member;
	
	// 추천된 날짜 필드 추가
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date recommendationDate;

    // 추천된 날짜가 기본적으로 저장되도록 설정하는 생성자 추가
    @PrePersist
    public void prePersist() {
        if (recommendationDate == null) {
            recommendationDate = new Date();  // 현재 시간으로 설정
        }
    }
}
