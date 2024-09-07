package com.demo.domain;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DynamicInsert 
@DynamicUpdate
public class Review {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reviewseq")
    @SequenceGenerator(name = "reviewseq", sequenceName = "reviewseq", allocationSize = 1)	
	int review_seq; //글번호 고유
	
	private String kakao_id; //(카카오맵에서 가져오기)
	private String kakao_name; //(카카오맵에서 가져오기) - 머리말
	private String title; //글제목
	
	@OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})// save, update만 가능 (del불가)
    @JoinColumn(name="id", nullable=false)
	private MemberData member_data; //글쓴이아이디 (멤버테이블에서 가져오기)
	
	@Lob
    @Column(name = "content", columnDefinition = "CLOB")
	private String content; //글내용
	
	@Temporal(value=TemporalType.TIMESTAMP)
    @ColumnDefault("sysdate")
	private Date write_date; //작성날짜
	
	private int cnt; //조회수
	private int goodpoint; // 추천수
	private int bookmark; // 북마크수
	private int reviewrate; //별점
	
	@ElementCollection
    private List<String> images;
	

}
