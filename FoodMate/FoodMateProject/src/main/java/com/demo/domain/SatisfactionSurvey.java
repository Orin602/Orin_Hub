package com.demo.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
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
public class SatisfactionSurvey {
	
	// 익명의 만족도 조사
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int VerySatisfied;
    private int Satisfied;
    private int Neutral;
    private int Dissatisfied;
    private int VeryDissatisfied;
    private LocalDateTime createdAt;
    
    // 익명의 의견 제출
    private String Opinion;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }


    // getters and setters
}
