package com.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.demo.domain.Notice;
import com.demo.persistence.NoticeRepository;

@Service
public class NoticeServiceImpl implements NoticeService {
	
	@Autowired
	NoticeRepository noticeRepo;

	@Override
	public void insertNotice(Notice vo) {
		noticeRepo.save(vo);

	}

	@Override
	public void updateNotice(Notice vo) {
		Notice p = noticeRepo.getNotice(vo.getNotice_seq());
		vo.setNotice_date(p.getNotice_date());
		vo.setNotice_seq(p.getNotice_seq());
		noticeRepo.save(vo);

	}

	@Override
	public void deleteNotice(Notice vo) {
		noticeRepo.delete(vo);

	}
	
	//클릭시
	@Override
	public Notice getNotice(int notice_seq) {
		return noticeRepo.getNotice(notice_seq);
	}
	
	//공지사항란 리스트
	@Override
	public List<Notice> getNotice() {
		return noticeRepo.getNoticeList();
	}

	@Override
	public Page<Notice> getAllNotice(int notice_seq, int page, int size) {
		Pageable pageable = PageRequest.of(page - 1, size, Direction.ASC, "notice_seq");
		return noticeRepo.findAllNotice(notice_seq, pageable);
	}

	@Override
	public Page<Notice> getNoticeByNotice_Title(int page, int size, String notice_title) {
		Pageable pageable = PageRequest.of(page - 1, size, Direction.ASC, "notice_seq");
		return noticeRepo.findNoticeByTitleContainingOrderByNotice_title(notice_title, pageable);
	}

	@Override
	public Page<Notice> getNoticeBynotice_cate(int page, int size, String notice_cate) {
		Pageable pageable = PageRequest.of(page - 1, size, Direction.ASC, "notice_seq");
		return noticeRepo.findNoticeByNotice_cateContaining(notice_cate, pageable);
	}
}
    
