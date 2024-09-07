package com.demo.service;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

import org.hibernate.query.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.demo.domain.MemberData;
import com.demo.domain.Review;
import com.demo.persistence.MemberRepository;
import com.demo.persistence.ReviewRepository;



@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository memberRepo;
    
    @Autowired
    private ReviewRepository reviewRepository; // 리뷰 Repository

    @Override
    public void insertMember(MemberData member) {
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

		memberRepo.updateMemberData(vo.getId(), vo.getPassword(), vo.getEmail());
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

	@Override
	public void changeBodyData(MemberData vo) {
		// TODO Auto-generated method stub
		
	}
	
	public void deleteMemberById(String id) {
        memberRepo.deleteById(id);
    }

	@Override
	public void deleteMember(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void withdrawMember(String id) {
		 // 회원을 찾습니다.
	    Optional<MemberData> optionalMember = Optional.ofNullable(memberRepo.findByLoginId(id));
	    
	    // 회원이 존재하면 삭제합니다.
	    optionalMember.ifPresent(member -> memberRepo.delete(member));
		
	}

	@Override
	public MemberData findById(String id) {
	    // 데이터베이스에서 사용자 ID를 기반으로 사용자를 조회하는 로직을 구현
	    // 여기서는 가상의 데이터를 사용하므로 간단한 예시를 제공합니다.
	    
	    // 예시: 사용자 ID가 id와 일치하는 MemberData 객체를 찾는다고 가정
	    // 실제로는 데이터베이스 쿼리 등을 사용하여 사용자를 조회해야 합니다.
	    MemberData memberData = memberRepo.findByLoginId(id);
	    
	    return memberData;
	}


	
	





}
