package com.demo.domain;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
public class Notice {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "noticeseq")
    @SequenceGenerator(name = "noticeseq", sequenceName = "noticeseq", allocationSize = 1)
	int notice_seq; //글번호 고유

	private String notice_title; //제목
	private String notice_cate; //공지종류 ( event, notice)
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="id", nullable=false)
	private MemberData member_data; 

	
	private String notice_content; //내용
	
	@Temporal(value=TemporalType.TIMESTAMP)
    @ColumnDefault("sysdate")
	private Date notice_date; //작성날짜
	
	private int notice_cnt; //조회수
	
	@ElementCollection
    private List<String> notice_images;

	public void setFileUrl(String string) {
		// TODO Auto-generated method stub
		
	}

}
