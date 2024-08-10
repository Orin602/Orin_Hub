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

	// 이미지 URL을 저장하기 위한 필드
    private String coverImageUrl;
    
	// 추가된 필드: 이미지 업로드를 위한 필드
    // 여러 이미지를 업로드할 수 있도록 List<String> 형태로 정의
    @ElementCollection
    private List<String> uploadedImages;
	
	@ManyToOne
	@JoinColumn(name="id")
	private Member member;	// member테이블 조회
	
	@Temporal(value=TemporalType.TIMESTAMP)
	@ColumnDefault("sysdate")
	private Date review_date; // 게시글작성 시간 저장(sysdate)
}
