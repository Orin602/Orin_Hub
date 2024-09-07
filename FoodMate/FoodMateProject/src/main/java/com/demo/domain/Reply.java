package com.demo.domain;
import java.util.Date;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.demo.domain.MemberData;

import jakarta.persistence.CascadeType;
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
public class Reply {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "replyseq")
    @SequenceGenerator(name = "replyseq", sequenceName = "replyseq", allocationSize = 1)
	private int replynum;
	
	private String content;
	
	@ManyToOne
    @JoinColumn(name="seq", nullable=false)
	private Com_Board_Detail com_board_detail;
	
	@ManyToOne
    @JoinColumn(name="no_data", nullable=false)
	private MemberData member_data;
	
	@Temporal(value=TemporalType.TIMESTAMP)
    @ColumnDefault("sysdate")
    private Date r_regdate;

	


}