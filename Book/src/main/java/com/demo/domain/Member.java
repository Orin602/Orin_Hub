package com.demo.domain;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_seq")
	@SequenceGenerator(name="member_seq", sequenceName = "member_seq", allocationSize = 1)
	private long member_seq;
	
	@Column(unique = true)
	private String id; // 아이디
	private String pwd; // 비밀번호
	private String name; // 이름
	private String email; // 이메일
    private String telephone;
    private String provider;

	private String address; // 기본 주소
	private String addressDetail; // 상세 주소

	@ColumnDefault("0")
	private int membercode; // 회원 코드

	@Temporal(value = TemporalType.TIMESTAMP)
	@ColumnDefault(value = "CURRENT_TIMESTAMP")
	private Date signupDate; // 가입 날짜

	@ColumnDefault("0") // 탈퇴 요청 기본값을 0으로 설정 (대기)
	private int withdrawalRequest; // 회원 탈퇴 요청 상태

	@PrePersist
	protected void onCreate() {
		if (this.signupDate == null) {
			// 메서드를 사용하여 엔티티가 처음 저장될 때 signupDate 필드가 'null'이면 현재 날짜 및 시간을 설정
			this.signupDate = new Date();
		}
	}
}
