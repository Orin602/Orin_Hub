package com.demo.service;

import java.util.List;

import com.demo.domain.MemberData;

public interface MemberService {
	
	// 회원정보 상세 조회
	public MemberData getMember(String id);
	
	// 회원 로그인
	public int loginID(MemberData vo);
	
	// 이름과 이메일로 아이디 찾기
	public MemberData getIdByNameEmail(String name, String email);
	
	// 아이디와 이름과 이메일로 비밀번호 찾기
	public MemberData getPwdByIdNameEmail(String id, String name, String email);

	public List<MemberData> getMemberList(String name);

	public void insertMember(MemberData vo);
	
	public void insertMemberNaver(MemberData vo);
	
	// 아이디 중복 확인
	public int confirmID(String id);
	
	public int confirmEmail(String email);
	
	public int confirmNickname(String nickname);


	// 마이페이지
	// 개인정보 수정
	public void changeInfo(MemberData vo);
	// 바디데이터 수정
	public void changeBodyData(MemberData vo);
	



}