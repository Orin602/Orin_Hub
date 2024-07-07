package com.demo.domain;

import java.util.Date;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Builder
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commentseq")
    @SequenceGenerator(name = "commentseq", sequenceName = "commentseq", allocationSize = 1)
    private int comment_seq; // 댓글 ID

    @ManyToOne
    @JoinColumn(name = "id")
    private Member member; // 댓글 작성자 (외래키)

    @ManyToOne
    @JoinColumn(name = "review_seq")
    private Review review; // 리뷰 게시글 (외래키)

    @ManyToOne
    @JoinColumn(name = "recommend_seq")
    private Recommend recommend; // 추천 게시글 (외래키)

    private String content; // 댓글 내용

    @Temporal(TemporalType.TIMESTAMP)
    @ColumnDefault("sysdate")
    private Date write_date; // 댓글 작성일

    // 기타 필드 추가 가능
}
