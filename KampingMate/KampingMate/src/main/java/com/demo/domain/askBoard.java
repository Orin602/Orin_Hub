package com.demo.domain;

// 'askBoard'라는 엔터티 클래스로, 고객의 1:1 문의 데이터를 표현.
// JPA (Java Persistence API)를 사용하여 데이터베이스 테이블과 매핑

import java.util.Date;

import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// 이 클래스가 JPA 엔터티임을 나타냄.
// 이 클래스는 데이터베이스 테이블과 매핑
@Entity
// Lombok을 사용하여 모든 필드에 대해 자동으로 getter와 setter 메서드를 생성
@Getter
@Setter
// Lombok을 사용하여 모든 필드를 포함하는 toString() 메서드를 자동으로 생성
@ToString
// Lombok을 사용하여 파라미터가 없는 기본 생성자를 생성
@NoArgsConstructor
// Lombok을 사용하여 모든 필드를 파라미터로 받는 생성자를 생성
@AllArgsConstructor
// Hibernate에서 사용할 수 있는 어노테이션으로, 엔터티의 변경된 필드만을 포함하는
// SQL 문을 생성. 이는 성능 최적화에 도움이 됨.
@DynamicInsert
@DynamicUpdate
public class askBoard {

	// 이 필드가 엔터티의 기본 키
    @Id
    // 기본 키의 생성 전략을 IDENTITY로 설정.
    // 이는 데이터베이스가 자동으로 기본 키 값을 생성
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long inquiry_id; // 1:1 문의 ID
    private String name; // 작성자 이름
    private String email; // 작성자 이메일
    private String message; // 문의 내용
    
    // Hibernate 어노테이션으로, 이 컬럼의 기본 값을 시스템의 현재 날짜
    // ('sysdate')로 설정. 이 필드는 문의가 등록된 날짜를 저장
    @ColumnDefault("sysdate")
    private Date regdate; // 등록일
    
    private String subject; // 문의 제목
    private String comments; // 관리자 답변 내용
    private String status; // 문의 상태

    // 이 엔터티가 다른 엔터티와 다대일 관계임을 나타낸다.
    // 하나의 'MemberData' 엔터티는 여러 개릐 'askBoard' 엔터티와 연결
    @ManyToOne
    // 외래 키 컬럼을 지정
    // 외래 키는 'member_data_id'이며, 이는 null 값을 가질 수 없음
    
    @JoinColumn(name = "member_data_id", nullable = false)
    private MemberData member_data; // 문의를 작성한 회원 정보를 참조

	public Long getId() {
		// TODO Auto-generated method stub
		return null;
	}

}
