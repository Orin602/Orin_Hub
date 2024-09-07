package com.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.demo.domain.CommunityBoard;

public interface CommunityBoardService {
	
	public List<CommunityBoard> getCommunityBoardList();
	
	public CommunityBoard getCommunityBoard(int community_seq);
	
	public void insertBoard(CommunityBoard vo);
	
	public void updateBoard(CommunityBoard vo);
	
	public void deleteBoard(CommunityBoard vo);
	
	public Page<CommunityBoard> getAllCommunityBoard(int community_seq, int page, int size);
	
	public Page<CommunityBoard> getCommunityBoardByTitle(int community_seq, int page, int size, String title);
	
	public Page<CommunityBoard> getCommunityBoardById(int community_seq, int page, int size, String id);
	
	public CommunityBoard getCommunityBoardByCommunity_seq(String communitySeq);
	
	
}
