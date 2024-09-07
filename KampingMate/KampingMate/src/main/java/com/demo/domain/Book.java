package com.demo.domain;

import java.util.Date;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
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
public class Book {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookseq")
    @SequenceGenerator(name = "bookseq", sequenceName = "bookseq", allocationSize = 1)
	private int bookseq;
	
	@OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})// save, update만 가능 (del불가)
    @JoinColumn(name="id", nullable=false)
	private MemberData member_data;
	
	private Date bookdateS; //시작
	private Date bookdateE; //종료
	
	private String campingname; 
	private String campingid;
	private String message; // 코멘트, 남길말
	private String headcount; //인원수
	
	private int condition; // 예약상태 0:대기 1:확정 2:불가

}
