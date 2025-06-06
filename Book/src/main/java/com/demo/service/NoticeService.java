package com.demo.service;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.demo.domain.Notice;

public interface NoticeService {

	// 공지사항 작성
	Notice createNotice(Notice notice);
	// 공지사항 삭제
	void deleteNotice(int notice_seq);
	// 공지사항 수정
	void updateNotice(Notice vo);
	// 공지사항 조회
	List<Notice> getAllNotices(Sort sort);
	Notice getNoticeById(int notice_seq);
	// 모든 공지사항 가져오기 (정렬되지 않음)
    List<Notice> getAllNotices(); // 새로 추가된 메서드
	// 공지사항 조회수 증가(viewCount)
	void increaseViewCount(int notice_seq, String sessionId);
	// 공지사항 좋아요(likeCount)
	void increaseLikeCount(int notice_seq, String id);
	
	void deleteImage(int notice_seq, int imageIndex);
}
