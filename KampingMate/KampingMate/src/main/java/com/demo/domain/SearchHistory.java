package com.demo.domain;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Table(name = "SearchHistory", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"contentId", "no_data"})
})
public class SearchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "searchHistory_generator")
    @SequenceGenerator(name="searchHistory_generator", sequenceName = "HISTORY_SEQ", allocationSize = 1)
    private int history_id;

    @ManyToOne
    @JoinColumn(name = "contentId")
    private GoCamping gocamping;

    @ManyToOne
    @JoinColumn(name = "no_data")
    private MemberData member;
}
