package com.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.domain.CommunityBoard;
import com.demo.persistence.CommunityBoardRepository;

@Service
public class CommunityBoardServiceImpl implements CommunityBoardService{

	@Autowired
	CommunityBoardRepository communityRepo;
	
	// 게시글 목록
	@Override
	public List<CommunityBoard> getCommunityBoardList() {
		return communityRepo.getBoard_List();
	}

	// 게시글 상세정보
	@Override
	public CommunityBoard getCommunityBoard(int community_seq) {
		return communityRepo.getCommunityBoard(community_seq);
	}
	//게시글 정보 저장
	@Override
	public void insertBoard(CommunityBoard vo) {
		communityRepo.save(vo);

	}
	
	@Transactional
	@Override
	public void updateBoard(CommunityBoard vo) {
		CommunityBoard p = communityRepo.findById(vo.getCommunity_seq()).get();
		vo.setCommunity_d_regdate(p.getCommunity_d_regdate());
		vo.setCommunity_cnt(p.getCommunity_cnt());
		communityRepo.save(vo);

	}
	
	@Override
	public void deleteBoard(CommunityBoard vo) {
		communityRepo.delete(vo);

	}

	// 페이징처리
	@Override
	public Page<CommunityBoard> getAllCommunityBoard(int community_seq, int page, int size) {
		Pageable pageable = PageRequest.of(page - 1, size, Direction.ASC, "community_seq");
		return communityRepo.findAllCommunityBoard(community_seq, pageable);
	}

	// 제목으로 검색
	@Override
	public Page<CommunityBoard> getCommunityBoardByTitle(int community_seq, int page, int size, String title) {
		Pageable pageable = PageRequest.of(page - 1, size, Direction.ASC, "community_seq");
		return communityRepo.findCommunityBoardByTitleContainingOrderByTitle(title, community_seq, pageable);
	}

	// 아이디로검색
	@Override
	public Page<CommunityBoard> getCommunityBoardById(int community_seq, int page, int size, String id) {
		Pageable pageable = PageRequest.of(page - 1, size, Direction.ASC, "community_seq");
		return communityRepo.findCommunityBoardByIdContainingOrderById(id, community_seq, pageable);
	}
	
	
	//pdf
	 public CommunityBoard getCommunityBoardByCommunity_seq(String communitySeq) {
		 
	        CommunityBoard communityBoard = communityRepo.findCommunityBoardByCommunity_seq(communitySeq); 

	        return communityBoard;
	    }


	
}
