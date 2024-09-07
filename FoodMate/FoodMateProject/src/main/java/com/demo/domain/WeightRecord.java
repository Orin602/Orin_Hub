package com.demo.domain;

import java.util.Date;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "weight_record")
@NoArgsConstructor
@AllArgsConstructor
public class WeightRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long re_id;
    
    @Temporal(TemporalType.DATE)
    @ColumnDefault("sysdate")
    private Date re_date; // 체중 기록 날짜
    
    @Column(nullable = false)
    private Double re_weight; // 기록한 체중
    
    @Temporal(TemporalType.TIMESTAMP)
    @ColumnDefault("sysdate")
    private Date createdAt; // 생성 시각을 자동 기록하기 위한 필드
    
    @Temporal(TemporalType.TIMESTAMP)
    @ColumnDefault("sysdate")
    private Date updatedAt; // 수정 시간을 자동 기록하기 위한 필드
    
    @ManyToOne // MemberData와의 다대일 관계 설정. 외래 키는 no_data로 설정
    @JoinColumn(name="no_data", nullable=false)
    private MemberData member;
    
    @PrePersist
    protected void onCreate() {
        Date now = new Date();
        this.createdAt = now;
        this.updatedAt = now;
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }
}
