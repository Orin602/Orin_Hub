package com.demo.domain;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
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
    private String id;  // 아이디

    private String pwd;  // 비밀번호
    private String name;  // 이름
    private String email;  // 이메일
    private String nickname;  // 닉네임
    
    @ColumnDefault("0")
    private int membercode;	// 회원 코드

    @Temporal(value = TemporalType.TIMESTAMP)
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    private Date signupDate;  // 가입 날짜

    @PrePersist
    protected void onCreate() {
        if (this.signupDate == null) {
        	// 메서드를 사용하여 엔티티가 처음 저장될 때 signupDate 필드가 'null'이면 현재 날짜 및 시간을 설정
            this.signupDate = new Date();
        }
    }
}
