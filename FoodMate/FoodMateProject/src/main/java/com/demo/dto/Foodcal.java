package com.demo.dto;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DynamicInsert 
@DynamicUpdate
public class Foodcal {
	private String foodcate;
	@Id
	private String foodname;
	private String g; //그람수
	private String kcal;
	private String carbohydrate;
	private String protein;
	private String province;
	private String sugars;
	private String salt;
	private String cholesterol;
}
