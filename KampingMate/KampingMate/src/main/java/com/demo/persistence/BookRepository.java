package com.demo.persistence;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.domain.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {
	
	// 해당 예약 출력
	@Query(value="SELECT * FROM Book WHERE bookseq = ?1 ", nativeQuery=true)
	public Book getBook(int bookseq); 
	
	 //나의 예약 기록
	@Query(value="SELECT b.id , b.bookseq , b.bookdateS, b.bookdateE, b.campingname, b.campingid, b.message, b.headcount, b.condition FROM Book b JOIN Member_Data m ON b.id = m.no_data WHERE b.id = ?1", nativeQuery=true)
	public Page<Book> getMyBookList(int id, int bookseq, Pageable pageable); 

	// 시작날짜로 검색 //미완
	@Query(value="SELECT * FROM Book WHERE BookdateS LIKE %?1% ", nativeQuery=true)
	public Page<Book> findBookByBookdateSContainingOrderByBookdateS(Date BookdateS, int bookseq, Pageable pageable);
	
	// 종료날짜로 검색 //미완
	@Query(value="SELECT * FROM Book WHERE BookdateE LIKE %?1% ", nativeQuery=true)
	public Page<Book> findBookByBookdateEContainingOrderByBookdateE(Date BookdateE, int bookseq, Pageable pageable);
	
	//말머리 검색(캠핑장)
	@Query(value="SELECT * FROM Book WHERE campingname LIKE %?1% ", nativeQuery=true)
	public Page<Book> findBookBycampingnameContainingOrderBycampingname(String campingname, int bookseq, Pageable pageable);

}
