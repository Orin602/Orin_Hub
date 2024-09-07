package com.demo.domain;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class MemberRating {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rate_generator")
    @SequenceGenerator(name="rate_generator", sequenceName = "RATE_SEQ", allocationSize = 1)
    private int rate_idx;
    
    @ManyToOne
    @JoinColumn(name = "no_data")
    private MemberData no_data;
    
    @ManyToOne
    @JoinColumn(name = "content_id")
    private GoCamping contentId;
    
    private double rating;
}
