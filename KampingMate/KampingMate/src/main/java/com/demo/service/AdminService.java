package com.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.demo.domain.AdminQnaBoard;
import com.demo.domain.Book;
import com.demo.domain.MemberData;
import com.demo.domain.Notice;
import com.demo.domain.askBoard;

public interface AdminService {
	
	// 관리자인지 체크(id 및 유저코드)
	int adminCheck(MemberData vo);
	
	// 관리자인 경우 모든 멤버의 정보조회
	public List<MemberData> getAllMemberList();

	// 관리자인 경우 모든 Q&A게시판 정보조회 
	public Page<AdminQnaBoard> getAllQnaBoardList(Pageable pageable);
	public List<AdminQnaBoard> getAllQnaBoardListMain();
		
	// 관리자인 경우 모든 1:1문의게시판 정보조회
	public Page<askBoard> getAllAskBoardListWait(Pageable pageable);
	public Page<askBoard> getAllAskBoardListFinish(Pageable pageable);
	public List<askBoard> getAllAskBoardListMain();
	
	// 관리자인 경우 모든 공지사항게시판 정보조회 
	public List<Notice> getAllList(String string);

		public List<Notice> getAllListMain();
		public List<Notice> getAllNotices();

		public List<Notice> getAllEvents();
	
	// 게시글 저장
	
	void insertAdminQnaBoard(AdminQnaBoard vo);
	void insertNotice(Notice vo);
	
	// 게시글 수정
	
	public void updateAdminQnaBoard(AdminQnaBoard vo);
	
	public void updateAdminInquiry(askBoard vo);
	
	public void updateAdminNotice(Notice vo);
	
	// 게시글 삭제
	
	public void deleteAdminQnaBoard(long boardnum);
	
	public void deleteAdminNotice(int boardnum2);
	
	// 게시글 Boardnum을 통해서 게시글 조회
	public askBoard getByAskBoardnum(long boardnum);
	public AdminQnaBoard getByQnaBoardnum(long boardnum);
	public Notice getByNoticeBoardnum(int boardnum);

	MemberData validateLogin(String id, String password);

	void withdrawMember(String id);

	List<Book> getAllBookings();

	void updateBookingCondition(int bookseq, int condition);

	Notice getBySeq(int noticeSeq);

	void saveNotice(Notice notice);

	Page<Notice> getAllNotices(Pageable pageable);

	Page<Notice> getAllEvents(Pageable pageable);

	

	

	

	

	

	

	
	



	

	

	
}
