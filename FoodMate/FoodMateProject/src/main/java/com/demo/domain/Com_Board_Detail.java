package com.demo.domain;

import java.util.Date;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.demo.dto.Com_Recipe;
import com.demo.domain.MemberData;

import jakarta.persistence.CascadeType;
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
public class Com_Board_Detail {

			@Id
			@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "boardseq")
		    @SequenceGenerator(name = "boardseq", sequenceName = "boardseq", allocationSize = 1)
			private int seq; //게시글번호
			
			@OneToOne(cascade = CascadeType.REMOVE)
		    @JoinColumn(name="idx", nullable=false)
		    private Com_Recipe com_recipe; //레시피번호
		    
		    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})// save, update만 가능 (del불가)
		    @JoinColumn(name="no_data", nullable=false)
		    private MemberData member_data; //회원아이디
		    
		    @Temporal(value=TemporalType.TIMESTAMP)
		    @ColumnDefault("sysdate")
		    private Date d_regdate; //작성일자 
		    
		    private int cnt; //조회수
		    
		    private int goodpoint; // 추천수




}