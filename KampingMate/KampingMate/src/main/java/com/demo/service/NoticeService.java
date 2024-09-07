package com.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.demo.domain.Notice;
import com.demo.domain.Review;

public interface NoticeService {

	public void insertNotice(Notice vo);
	
	public void updateNotice(Notice vo);
	
	public void deleteNotice(Notice vo);

	public Notice getNotice(int notice_seq); //seq로 해당글 찾기
	
	public List<Notice> getNotice();
	
	public Page<Notice> getAllNotice(int notice_seq, int page, int size); //전체 페이징
	
	public Page<Notice> getNoticeByNotice_Title(int page, int size, String notice_title); // 제목으로검색
	
	public Page<Notice> getNoticeBynotice_cate(int page, int size, String notice_cate); //말머리(종류)으로 정렬
	
}
