package com.demo.domain;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
public class foodRecipe {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "food_idx_generator")
    @SequenceGenerator(name="food_idx_generator", sequenceName = "RECIPE_SEQ", allocationSize = 1)
    private Long idx;
    private String name;
    @Column(columnDefinition = "VARCHAR2(4000)")
    private String images;
    @Column(columnDefinition = "VARCHAR2(1000)")
    private String recipecategory;
    @Column(columnDefinition = "VARCHAR2(1000)")
    private String keywords;
    @Column(columnDefinition = "VARCHAR2(3000)")
    private String recipeingredientparts;
    @ColumnDefault("0")
    private BigDecimal calories;
    @ColumnDefault("0")
    private BigDecimal fatcontent;
    @ColumnDefault("0")
    private BigDecimal saturatedfatcontent;
    @ColumnDefault("0")
    private BigDecimal cholesterolcontent;
    @ColumnDefault("0")
    private BigDecimal sodiumcontent;
    @ColumnDefault("0")
    private BigDecimal carbohydratecontent;
    @ColumnDefault("0")
    private BigDecimal fibercontent;
    @ColumnDefault("0")
    private BigDecimal sugarcontent;
    @ColumnDefault("0")
    private BigDecimal proteincontent;
    @Column(columnDefinition = "VARCHAR2(4000)")
    private String recipeinstructions;
}
