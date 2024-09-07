package com.demo.domain;

import java.util.Date;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
public class AdminQnaBoard {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qnanum_generator")
    @SequenceGenerator(name="qnanum_generator", sequenceName = "QNANUM_SEQ", allocationSize = 1)
    private long qna_boardnum;
	private String question;
	private String answer;
	@Temporal(value=TemporalType.TIMESTAMP)
    @Column(columnDefinition = "DATE default sysdate")
    private Date regdate;
}
