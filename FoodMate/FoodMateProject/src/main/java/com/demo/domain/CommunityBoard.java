package com.demo.domain;

import java.util.Date;

import org.hibernate.annotations.ColumnDefault;
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
import jakarta.persistence.Table;
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
public class CommunityBoard {

			@Id
			@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "community_seq_sec")
		    @SequenceGenerator(name = "community_seq_sec", sequenceName = "community_seq_sec", allocationSize = 1)
			private int community_seq; //게시글번호
		    
		    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})// save, update만 가능 (del불가)
		    @JoinColumn(name="no_data", nullable=false)
		    private MemberData member_data; //회원아이디
		    
		    private String community_title;
		    
		    private String community_content; //내용 
		    
		    @Temporal(value=TemporalType.TIMESTAMP)
		    @ColumnDefault("sysdate")
		    private Date community_d_regdate; //작성일자 
		    
		    private int community_cnt; //조회수
		    
		    private int community_goodpoint; // 추천수




}
