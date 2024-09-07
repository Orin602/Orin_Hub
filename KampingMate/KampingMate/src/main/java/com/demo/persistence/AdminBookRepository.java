package com.demo.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo.domain.Book;

public interface AdminBookRepository extends JpaRepository<Book, Integer> {

	@Query("SELECT b FROM Book b WHERE b.condition = :condition ORDER BY b.bookseq DESC")
    List<Book> findAllByCondition(@Param("condition") int condition);

    @Modifying
    @Query("UPDATE Book b SET b.condition = :condition WHERE b.bookseq = :bookseq")
    void updateBookCondition(@Param("bookseq") int bookseq, @Param("condition") int condition);
}
