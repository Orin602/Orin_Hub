package com.demo.persistence;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.domain.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {
	
	// 해당 공지사항 출력
	@Query(value="SELECT * FROM Notice WHERE notice_seq = ?1 ", nativeQuery=true)
	public Notice getNotice(int Notice_seq); 
	
	 //전체 공지사항 리스트 출력
	@Query(value="SELECT * FROM Notice ORDER BY notice_seq DESC ", nativeQuery=true)
	public List<Notice> getNoticeList();
	
	//제목으로 검색
	@Query(value="SELECT * FROM Notice WHERE notice_title LIKE %?1% ", nativeQuery=true)
	public Page<Notice> findNoticeByTitleContainingOrderByNotice_title(String title, Pageable pageable);
	
	//말머리 검색(이벤트, 공지)
	@Query(value = "SELECT * FROM Notice WHERE notice_cate = ?1", nativeQuery=true)
	public Page<Notice> findNoticeByNotice_cateContaining(String notice_cate, Pageable pageable);
	
	//전체 공지사항 페이징처리 
	@Query(value="SELECT * FROM Notice ORDER BY notice_seq DESC ", nativeQuery=true)
	public Page<Notice> findAllNotice(int Notice_seq, Pageable pageable); 


}
