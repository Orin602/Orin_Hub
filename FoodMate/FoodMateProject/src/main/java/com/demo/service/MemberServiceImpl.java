package com.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.domain.MemberData;
import com.demo.persistence.MemberRepository;



@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository memberRepo;

    @Override
    public void insertMember(MemberData member) {
        memberRepo.save(member);
    }
    
    @Override
    public void insertMemberNaver(MemberData member) {
    	String randomPassword = NaverPasswordGenerator.generateRandomPassword();
    	member.setPassword(randomPassword);
        memberRepo.save(member);
    }

    @Override
    public MemberData getMember(String id) {
        return memberRepo.findByLoginId(id);
    }

    @Override
    public int loginID(MemberData vo) {
        int result = -1;

        // Member_data 테이블에서 사용자 조회
        MemberData member = memberRepo.findByLoginId(vo.getId());
        
        // 결과값 설정: 
        // 1: ID,PWD 일치, 0: 비밀번호 불일치, -1: ID가 존재하지 않음.
        if (member != null) {
            if(member.getPassword().equals(vo.getPassword())) {
                result = 1;  // ID, 비밀번호 일치
            } else {
                result = 0;  // 비밀번호 불일치
            }
        } else {
            result = -1;    // ID가 존재하지 않음.
        }
        
        return result;
    }

    @Override
    public MemberData getIdByNameEmail(String name, String email) {
        return memberRepo.findByNameAndEmail(name, email);
    }

    @Override
    public MemberData getPwdByIdNameEmail(String id, String name, String email) {
        return memberRepo.findByIdAndNameAndEmail(id, name, email);
    }

    @Override
    public List<MemberData> getMemberList(String name) {
        return memberRepo.findByNameContaining(name);
    }

	@Override
	public int confirmID(String id) {
		int result = 0;
		
		MemberData member = memberRepo.findByLoginId(id);
		
		if(member != null) {
			result = 1;
		} else {
			result = -1;
		}
		
		return result;
	}

	@Override
	public int confirmEmail(String email) {
		int result = 0;
		
		MemberData member = memberRepo.findByEmail(email);
		
		if(member != null) {
			result = 1;
		} else {
			result = -1;
		}
		
		return result;
	}

	// 마이페이지용
	// 개인정보 수정
	@Override
	public void changeInfo(MemberData vo) {

		memberRepo.updateMemberData(vo.getId(), vo.getPassword(), vo.getNickname(), vo.getEmail());
	}
	
	// 바디데이터 수정
	@Override
	public void changeBodyData(MemberData vo) {
		memberRepo.updateBodyData(vo.getId(), vo.getHeight(), vo.getWeight(), vo.getBmi(), vo.getAge(), vo.getGender(), vo.getGoal(), vo.getGoalDay());
	}

	@Override
	public int confirmNickname(String nickname) {
		int result = 0;
		MemberData member = memberRepo.findByNickname(nickname);
		
		if(member != null) {
			result = 1;
		} else {
			result = -1;
		}
		return result;
	}

}
