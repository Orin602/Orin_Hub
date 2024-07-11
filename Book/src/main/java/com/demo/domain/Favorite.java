package com.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Favorite") // 테이블 이름 지정
public class Favorite {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    private Long id; // 즐겨찾기 고유 식별자

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member; // 사용자 ID

    @ManyToOne
    @JoinColumn(name = "review_seq")
    private Review review; // 즐겨찾기한 리뷰
	
}
