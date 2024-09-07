package com.demo.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.domain.Notice;

public interface AdminNoticeRepository extends JpaRepository<Notice, Integer> {

    @Query(value="SELECT * FROM notice ORDER BY notice_date DESC LIMIT 5", nativeQuery = true)
    List<Notice> getAllListMain();
    
    @Query(value="SELECT * FROM notice WHERE notice_seq = :boardnum2", nativeQuery = true)
    Notice findByNum(int boardnum2);
    
  //전체 공지사항 리스트 출력
//  	@Query(value="SELECT * FROM Notice ORDER BY notice_seq DESC ", nativeQuery=true)
//  	public List<Notice> getAllList();
  	
    @Query(value="SELECT * FROM Notice WHERE notice_cate = 'notice' ORDER BY notice_seq DESC", nativeQuery=true)
    public List<Notice> getAllNotices();

    @Query(value="SELECT * FROM Notice WHERE notice_cate = 'event' ORDER BY notice_seq DESC", nativeQuery=true)
    public List<Notice> getAllEvents();

    @Query(value = "SELECT * FROM notice WHERE notice_seq = :boardnum2", nativeQuery = true)
    Notice findBySeq(int boardnum2);
    
 // 공지사항만 가져오는 메서드 (native query 사용)
    @Query(value = "SELECT * FROM Notice WHERE notice_cate = ?1 ORDER BY notice_seq DESC", nativeQuery = true)
    List<Notice> getAllNotices(String noticeCate);

    // 공지사항만 가져오는 메서드 (페이징)
    @Query(value = "SELECT * FROM Notice WHERE notice_cate = ?1 ORDER BY notice_seq DESC",
           countQuery = "SELECT COUNT(*) FROM Notice WHERE notice_cate = ?1",
           nativeQuery = true)
    Page<Notice> getAllNotices(String noticeCate, Pageable pageable);


}
