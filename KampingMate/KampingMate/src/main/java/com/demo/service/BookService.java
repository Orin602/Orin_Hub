package com.demo.service;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.demo.domain.Book;

public interface BookService {
	
	public void insertBook(Book vo);
	
	public void updateBook(Book vo);
	
	public void deleteBook(Book vo);
	
	// 예약출력
	public Page<Book> getAllBook(int bookseq, int page, int size, int id); 
	
	public Book getBook(int bookseq); 
	
	//캠핑장명으로 검색
	public Page<Book> getBookByCamping(int bookseq, int page, int size, String campingname);
	
	//시작 종료일자별 검색
	public Page<Book> getBookByBookdateS(int bookseq, int page, int size, Date bookdateS);
	
	public Page<Book> getBookByBookdateE(int bookseq, int page, int size, Date bookdateE);

}
