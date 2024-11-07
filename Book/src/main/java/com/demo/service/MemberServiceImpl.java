package com.demo.service;

import java.util.List;

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
	public int adminId(Member vo) {
		int result = -1;
		// 회원 조회
		Member member = memberRepo.findByLoginId(vo.getId());
		
		if(member != null) { // 아이디 존재
			if(member.getPwd().equals(vo.getPwd())) { // pwd 일치
				if(member.getMembercode() == 1) { // 관리자 코드 1
					result = 1;
				} else {
					result = 2;
				}
			} else { // pwd 불일치
				result =-1;
			}
		} else { // id 없음
			result = 0;
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

	// 관리자용
	@Override
	public List<Member> getAllMembers() {
		return memberRepo.getAllMember();
	}
	@Override
	public Member getMemberById(String id) {
		return memberRepo.findMemberById(id);
	}
	// 회원코드 수정 메서드
    @Override
    public void updateMemberCode(String id, int memberCode) {
        Member member = memberRepo.findMemberById(id);
        if (member != null) {
            member.setMembercode(memberCode);
            memberRepo.save(member); // 변경된 회원 정보를 저장
        }
    }
    @Override
    public boolean processDeleteRequest(String id, String pwd) {
        int updatedRows = memberRepo.updateWithdrawalRequest(id, pwd, 1); // 탈퇴 요청 값 1로 업데이트
        return updatedRows > 0; // 업데이트 성공 여부 반환
    }

}
