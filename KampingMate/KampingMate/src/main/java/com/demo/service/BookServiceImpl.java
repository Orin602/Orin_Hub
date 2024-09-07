package com.demo.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.demo.domain.Book;
import com.demo.persistence.BookRepository;

@Service
public class BookServiceImpl implements BookService {
	
	@Autowired
	BookRepository bookRepo;

	@Override
	public void insertBook(Book vo) {
		bookRepo.save(vo);

	}
	
	@Override
	public void updateBook(Book vo) {
		Book book = bookRepo.getBook(vo.getBookseq());
		vo.setBookseq(book.getBookseq());
		bookRepo.save(vo);

	}

	@Override
	public void deleteBook(Book vo) {
		bookRepo.delete(vo);

	}

	//예약리스트	
	@Override
	public Page<Book> getAllBook(int bookseq, int page, int size, int id) {
		Pageable pageable = PageRequest.of(page - 1, size, Direction.ASC, "bookseq");
		return bookRepo.getMyBookList(id, bookseq, pageable);
	}
	
	//상세정보 (클릭시)
	@Override
	public Book getBook(int bookseq) {
		return bookRepo.getBook(bookseq);
	}
	
	//캠핑장명으로 검색
	@Override
	public Page<Book> getBookByCamping(int bookseq, int page, int size, String campingname) {
		Pageable pageable = PageRequest.of(page - 1, size, Direction.ASC, "bookseq");
		return bookRepo.findBookBycampingnameContainingOrderBycampingname(campingname, bookseq, pageable);
	}
	
	//시작일자 
	@Override
	public Page<Book> getBookByBookdateS(int bookseq, int page, int size, Date bookdateS) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//종료일자
	@Override
	public Page<Book> getBookByBookdateE(int bookseq, int page, int size, Date bookdateE) {
		// TODO Auto-generated method stub
		return null;
	}

}
