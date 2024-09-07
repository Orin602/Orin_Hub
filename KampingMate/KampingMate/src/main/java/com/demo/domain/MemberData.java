package com.demo.domain;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class MemberData {
   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_generator")
   @SequenceGenerator(name="member_generator", sequenceName = "MEMBER_SEQ", allocationSize = 1)
   private long no_data;
   
   @ColumnDefault("0")
   private long usercode;
   
   @Column(unique = true)
    private String id;
    private String password;
    private long age;
    private String name;
    private String email;
    private String telephone;
    private String provider;
    
    @OneToMany(mappedBy = "member_data", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();
    
}