package com.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.domain.Member;
import com.demo.persistence.MemberRepository;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberRepository memberRepo;
	

	@Override
	public void insertMember(Member vo) {
		memberRepo.save(vo);
	}

	@Override
	public Member getMember(String id) {
		return memberRepo.findByLoginId(id);
	}

	@Override
	public int loginId(Member vo) {
		int result = -1;
		
		// 회원 조회
		Member member = memberRepo.findByLoginId(vo.getId());
		
		if (member != null) {
			if(member.getPwd().equals(vo.getPwd())) {
				result = 1;	// id, pwd 일치
			} else {
				result = 0;	// pwd 불일치
			}
		} else {
			result = -1;	// id 없음
		}
		return result;
	}

	@Override
	public Member findId(String name, String email) {
		return memberRepo.findByNameAndEmail(name, email);
	}

	@Override
	public Member findPwd(String id, String name, String email) {
		return memberRepo.findByIdAndNameAndEmail(id, name, email);
	}

	@Override
	public int confirmId(String id) {
		int result = 0;
		
		Member member = memberRepo.findByLoginId(id);
		
		if (member != null) {
			result = 1;
		} else {
			result = -1;
		}
		return result;
	}

	@Override
	public int confirmNickname(String nickname) {
		int result = 0;
		
		Member member = memberRepo.findByNickname(nickname);
		if (member != null) {
			result = 1;
		} else {
			result = -1;
		}
		return result;
	}
	
	@Override
	public int confirmEmail(String email) {
		int result = 0;
		Member member = memberRepo.findByEmail(email);
		if(member != null) {
			result = 1;
		} else {
			result = -1;
		}
		return result;
	}

	@Override
	public void changeInfo(Member vo) {
		memberRepo.updateMember(vo.getId(), vo.getPwd(), vo.getNickname());
	}

	@Override
	public void deleteMember(Member vo) {
		memberRepo.deleteMemberByIdAndPassword(vo.getId(), vo.getPwd());
	}

}
