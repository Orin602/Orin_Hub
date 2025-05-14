package com.demo.service;

import java.util.List;

import com.demo.domain.Member;

public interface MemberService {

	// 회원가입
	public void insertMember(Member vo);
	
	// 회원정보 조회
	public Member getMember(String id);
	
	// 로그인
	public int loginId(Member vo);
	// 관리자 로그인 
	public int adminId(Member vo);
	
	// 아이디찾기 (이름, 이메리)
	public Member findId(String name, String email);
	
	// 비밀번호찾기 (아이디, 이름, 이메일)
	public Member findPwd(String id, String name, String email);
	
	// 아이디 중복체크
	public int confirmId(String id);
	
	// 닉네임 중복체크
	public int confirmNickname(String nickname);
	
	// 이메일 중복체크
	public int confirmEmail(String email);
	
	// 개인정보 수정
	public void changeInfo(Member vo);
	
	// 회원 탈퇴
	public void deleteMember(Member vo);
	
	// 관리자용
	// 전체 회원 목록 조회
	List<Member> getAllMembers();
	// 특정 회원 조회 (검색)
	Member getMemberById(String id);
	// 회원코드 수정
    void updateMemberCode(String id, int memberCode);

	boolean processDeleteRequest(String id, String pwd);

}
