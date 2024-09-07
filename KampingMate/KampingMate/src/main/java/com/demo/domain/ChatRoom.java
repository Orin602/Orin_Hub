package com.demo.domain;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
public class ChatRoom {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "room_generator")
	@SequenceGenerator(name="room_generator", sequenceName = "ROOM_SEQ", allocationSize = 1)
	private int room_seq;
	private String title;
	
}
