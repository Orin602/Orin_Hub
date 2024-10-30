package com.demo.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.domain.FileUploadUtil;
import com.demo.domain.Notice;
import com.demo.domain.Review;
import com.demo.persistence.NoticeRepository;

@Service
public class NoticeServiceImpl implements NoticeService {
	
	@Autowired
	private NoticeRepository noticeRepo;
	// 세션을 통한 조회수 중복 방지
    private final Set<String> viewedNotices = new HashSet<>();

	@Override
	public Notice createNotice(Notice notice) {
		return noticeRepo.save(notice);
	}

	@Override
	public void deleteNotice(int notice_seq) {
		noticeRepo.deleteById(notice_seq);
	}

	@Override
    public void updateNotice(Notice vo) {
        Notice update_notice = noticeRepo.getNoticeBySeq(vo.getNotice_seq());

        vo.setMember(update_notice.getMember());
        vo.setNotice_seq(update_notice.getNotice_seq());
        vo.setMember(update_notice.getMember());
        
       noticeRepo.save(update_notice);
    }

	@Override
	public List<Notice> getAllNotices(Sort sort) {	// sort별 조회
		return noticeRepo.findAll(sort);
	}

	@Override
	public Notice getNoticeById(int notice_seq) {	// 단일 조회
		return noticeRepo.findById(notice_seq)
				.orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다."));
	}

	@Override
	public void increaseViewCount(int notice_seq, String sessionId) {
		String viewedKey = sessionId + "_" + notice_seq;
        if (!viewedNotices.contains(viewedKey)) {
            viewedNotices.add(viewedKey); // 중복 조회 방지
            Notice notice = getNoticeById(notice_seq);
            notice.setViewCount(notice.getViewCount() + 1);
            noticeRepo.save(notice);
        }

	}

	@Override
	public void increaseLikeCount(int notice_seq, String id) {
		// 공지사항 조회
	    Notice notice = getNoticeById(notice_seq);
	    
	    // 사용자가 이미 좋아요를 눌렀는지 확인
	    if (!hasUserLikedNotice(notice_seq, id)) {
	        // 좋아요 수 증가
	        notice.setLikeCount(notice.getLikeCount() + 1);
	        // 변경된 notice 저장
	        noticeRepo.save(notice);
	    } else {
	        // 사용자가 이미 좋아요를 눌렀을 경우 좋아요 수 감소 (선택적)
	        notice.setLikeCount(notice.getLikeCount() - 1);
	        noticeRepo.save(notice);
	    }

	}
	// 사용자가 이미 공지사항에 좋아요를 눌렀는지 확인하는 메서드 (추후 LikeRepository 사용 시 구현)
    private boolean hasUserLikedNotice(int notice_seq, String id) {
        // LikeRepository가 있을 경우, existsByNoticeSeqAndUserId를 호출하여 체크
        return false; // 임시로 false 반환 (중복 좋아요가 없다고 가정)
    }

	@Override
	public List<Notice> getAllNotices() {
		return noticeRepo.findAll();
	}

	@Override
	@Transactional
	public void deleteImage(int notice_seq, int imageIndex) {
		Notice notice = noticeRepo.getNoticeBySeq(notice_seq);
		List<String> uploadImages = notice.getUploadedImages();
		
		if (imageIndex >= 0 && imageIndex < uploadImages.size()) {
	        // 이미지 리스트에서 해당 인덱스의 이미지 삭제
	        String imageUrlToRemove = uploadImages.remove(imageIndex);
	        
	        try {
	            // 파일 시스템에서 이미지 삭제
	            FileUploadUtil.deleteFile(imageUrlToRemove);
	        } catch (IOException e) {
	            // IOException을 RuntimeException으로 전환하여 처리
	            throw new RuntimeException("Failed to delete image file: " + imageUrlToRemove, e);
	        }
		       notice.setUploadedImages(uploadImages);
		       noticeRepo.save(notice);
		} else {
	        throw new IllegalArgumentException("Invalid image index: " + imageIndex);
	    }
	
	}
}