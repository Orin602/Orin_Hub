package com.demo.domain;

import java.util.Date;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
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

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@DynamicInsert
@DynamicUpdate
@Entity
public class Store {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "storeseq")
	@SequenceGenerator(name = "storeseq", sequenceName = "storeseq", allocationSize = 1)
	private int storeseq;

	@ManyToOne
	@JoinColumn(name = "member_seq")
	private Member member;
	private String address;
	
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date orderDate;

	private int EA;
	private int price;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	
	private String title;
    private String author;
    private String imageUrl;
    private String docId;
    private String kdcName1s;
    private String link;

}
