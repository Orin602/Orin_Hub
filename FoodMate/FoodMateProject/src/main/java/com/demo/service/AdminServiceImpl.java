package com.demo.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.demo.domain.AdminQnaBoard;
import com.demo.domain.AdminRecipeBoard;
import com.demo.domain.MemberData;
import com.demo.domain.askBoard;
import com.demo.domain.foodRecipe;
import com.demo.persistence.AdminAskBoardRepository;
import com.demo.persistence.AdminQnaBoardRepository;
import com.demo.persistence.AdminRecipeBoardRepository;
import com.demo.persistence.AdminRecipeDBRepository;
import com.demo.persistence.MemberRepository;

/*
 * 체크할 사항
 * member테이블 완성되고 로그인 과정 완성되면 adminCheck(), 회원관리페이지
 * 커뮤니티 inquiry 되면 테이블 조회 및 답변기능
 */

@Service
public class AdminServiceImpl implements AdminService {

/*
 * Repository생성
 */
	@Autowired
	private AdminRecipeBoardRepository adminRecipeBoardRepo;
	
	@Autowired
	private AdminQnaBoardRepository adminQnaBoardRepo;
	
	@Autowired
	private AdminRecipeDBRepository adminRecipeDBRepo;
	
	@Autowired
	private AdminAskBoardRepository adminAskBoardRepo;
	
	@Autowired
	private MemberRepository memberRepo;

	@Override
	public int adminCheck(MemberData vo) {
		int result = -1;
//		
//		// Member 테이블에서 사용자 조회
//		Optional<Member> member = memberRepo.findById(vo.getId());
//		
//		// 결과값 설정 (1:ID, PWD 일치 / 0: PWD 불일치 / -1: ID가 존재하지 않음)
//		// 멤버 전체에 대하여 사용자 객체의 id를 조회하고
//		// id가 존재할경우 해당 id의 유저코드를 가져와서 관리자(1)인 경우 1을 반환
//		if(member.isEmpty()) {
//			result = -1;
//		} else {
//			if(member.get().getUsercode().equals(1)) {
//				return result = 1;
//			} else {
//				return result = -1;
//			}
//		}
		return result;
	}

	/*
	 * 각 페이지 리스트 불러오기 서비스
	 */
	
	@Override
	public List<MemberData> getAllMemberList() {
		// TODO Auto-generated method stub
		return memberRepo.getAllMember();
	}
	
	@Override
	public Page<AdminRecipeBoard> getAllFoodBoardList(Pageable pageable) {
		
		// regdate 컬럼을 기준으로 내림차순 정렬하는 Sort 객체 생성
	    Sort sort = Sort.by(Sort.Direction.DESC, "regdate");
	    // 페이징과 정렬 정보를 포함하는 Pageable 객체 생성
	    Pageable sortedByRegdateDesc = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		
	    return adminRecipeBoardRepo.findAll(sortedByRegdateDesc);
	}
	
	@Override
	public List<AdminRecipeBoard> getAllFoodBoardListMain() {
		return adminRecipeBoardRepo.getAllRecipeListMain();
	}

	@Override
	public Page<AdminQnaBoard> getAllQnaBoardList(Pageable pageable) {
		
		// regdate 컬럼을 기준으로 내림차순 정렬하는 Sort 객체 생성
	    Sort sort = Sort.by(Sort.Direction.DESC, "regdate");
	    // 페이징과 정렬 정보를 포함하는 Pageable 객체 생성
	    Pageable sortedByRegdateDesc = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		
	    return adminQnaBoardRepo.findAll(sortedByRegdateDesc);
	}
	
	@Override
	public List<AdminQnaBoard> getAllQnaBoardListMain() {
		// TODO Auto-generated method stub
		return adminQnaBoardRepo.getAllQnaListMain();
	}

	@Override
	public Page<askBoard> getAllAskBoardListWait(Pageable pageable) {
		// regdate 컬럼을 기준으로 내림차순 정렬하는 Sort 객체 생성
	    Sort sort = Sort.by(Sort.Direction.DESC, "regdate");
	    // 페이징과 정렬 정보를 포함하는 Pageable 객체 생성
	    Pageable sortedByRegdateDesc = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		
	    return adminAskBoardRepo.findAllByStatus("답변 대기", sortedByRegdateDesc);
	}
	
	@Override
	public Page<askBoard> getAllAskBoardListFinish(Pageable pageable) {
		// regdate 컬럼을 기준으로 내림차순 정렬하는 Sort 객체 생성
	    Sort sort = Sort.by(Sort.Direction.DESC, "regdate");
	    // 페이징과 정렬 정보를 포함하는 Pageable 객체 생성
	    Pageable sortedByRegdateDesc = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		
	    return adminAskBoardRepo.findAllByStatus("답변 완료", sortedByRegdateDesc);
	}
	
	@Override
	public List<askBoard> getAllAskBoardListMain() {
		// TODO Auto-generated method stub
		return adminAskBoardRepo.getAllAskListMain();
	}
	
	@Override
	public List<foodRecipe> getRecipeDBListByName(String searchWord) {
		// TODO Auto-generated method stub
		return adminRecipeDBRepo.getRecipeDBListByName(searchWord);
	}

	@Override
	public List<foodRecipe> getRecipeDBListByIndex(String searchWord) {
		// TODO Auto-generated method stub
		return adminRecipeDBRepo.getRecipeDBListByIndex(searchWord);
	}

	/*
	 * *****등록/수정/삭제 서비스*****
	 */
	
	@Override
	public void insertAdminRecipeBoard(AdminRecipeBoard vo) {
		adminRecipeBoardRepo.save(vo);
		
	}
	
	@Override
	public void insertAdminQnaBoard(AdminQnaBoard vo) {
		adminQnaBoardRepo.save(vo);
		
	}
	
	@Override
	public void insertAdminRecipeDB(foodRecipe vo) {
		adminRecipeDBRepo.save(vo);
		
	}

	@Override
	public void updateAdminRecipeBoard(AdminRecipeBoard vo) {
		AdminRecipeBoard board = adminRecipeBoardRepo.findById(vo.getRecipe_boardnum()).get();
		
		vo.setRegdate(board.getRegdate());
		adminRecipeBoardRepo.save(vo);
	}
	
	@Override
	public void updateAdminQnaBoard(AdminQnaBoard vo) {
		
		vo.setRegdate(new Date());
		adminQnaBoardRepo.save(vo);
	}
	
	@Override
	public void updateAdminRecipeDB(foodRecipe vo) {
		
		adminRecipeDBRepo.save(vo);
		
	}


	@Override
	public void deleteAdminRecipeBoard(long boardnum) {
		adminRecipeBoardRepo.deleteById(boardnum);
		
	}
	
	@Override
	public void deleteAdminQnaBoard(long boardnum) {
		adminQnaBoardRepo.deleteById(boardnum);
		
	}
	
	@Override
	public void deleteDBData(long dbidx) {
		adminRecipeDBRepo.deleteById(dbidx);
		
	}

	/*
	 * 기타 쿼리문용 서비스
	 */
	
	@Override
	public AdminRecipeBoard getByRecipeBoardnum(long boardnum) {
		// TODO Auto-generated method stub
		return adminRecipeBoardRepo.findByRecipeBoardnum(boardnum);
	}
	
	@Override
	public AdminQnaBoard getByQnaBoardnum(long boardnum) {
		// TODO Auto-generated method stub
		return adminQnaBoardRepo.findByQnaBoardnum(boardnum);
	}
	
	@Override
	public askBoard getByAskBoardnum(long boardnum) {
		// TODO Auto-generated method stub
		return adminAskBoardRepo.findByAskBoardnum(boardnum);
	}

	@Override
	public foodRecipe getRecipeDBByIndex(long dbidx) {
		// TODO Auto-generated method stub
		return adminRecipeDBRepo.getRecipeDBByIndex(dbidx);
	}

	@Override
	public void updateAdminInquiry(askBoard vo) {
		adminAskBoardRepo.save(vo);
		
	}

	@Override
	public foodRecipe getFoodByIndex(int idx) {
		// TODO Auto-generated method stub
		return adminRecipeDBRepo.getFoodByIndex(idx);
	}

	


	

	

	

}
