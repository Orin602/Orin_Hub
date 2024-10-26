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
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Builder
@ToString
@Entity
public class Qna {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator= "qnaseq")
	@SequenceGenerator(name = "qnaseq", sequenceName= "qnaseq", allocationSize = 1)
	private int qna_seq;
	
	private String title;
	private String content;
	@Temporal(value=TemporalType.TIMESTAMP)
	@ColumnDefault("sysdate")
	private Date qna_date;
	
	private String answer;
	@Temporal(value=TemporalType.TIMESTAMP)
	private Date answer_date;
	// 답변 상태 : 0 = '답변 대기', 1 = '답변 완료'
	@ColumnDefault("0")
	private int answer_status;
	
	//
	@ManyToOne
	@JoinColumn(name="id")
	private Member member;
	
}
