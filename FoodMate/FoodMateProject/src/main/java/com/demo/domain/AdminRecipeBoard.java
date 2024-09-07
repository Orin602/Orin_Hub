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
public class AdminRecipeBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recipenum_generator")
    @SequenceGenerator(name="recipenum_generator", sequenceName = "RECIPENUM_SEQ", allocationSize = 1)
    private long recipe_boardnum;
    private String userid;
    private String nickname;
    private String title;
    private String content;
    private String images;
    private String tag;
	@Column(columnDefinition = "NUMBER default 0")
    private int count;
    @Temporal(value=TemporalType.TIMESTAMP)
    @Column(columnDefinition = "DATE default sysdate")
    private Date regdate;
    @Column(columnDefinition = "DATE default sysdate")
    private Date editdate;
	
	
	
}
