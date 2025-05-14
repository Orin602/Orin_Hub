package com.demo.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.demo.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	// 전체 회원목록 조회
	@Query(value="SELECT * FROM member ORDER BY signup_date DESC", nativeQuery=true)
	List<Member> getAllMember();
	
	// 특정 회원 조회
	@Query(value="SELECT * FROM member WHERE id =:id", nativeQuery=true)
	Member findMemberById(@Param("id") String id);
	
	// 일반회원 조회
	@Query("SELECT m FROM Member m WHERE m.membercode = 0")
	List<Member> findAllByMembercodeIsZero();

	
	// 아이디 찾기 (name, email)
	@Query(value="SELECT * FROM member WHERE name =:name AND email =:email", nativeQuery=true)
	Member findByNameAndEmail(String name, String email);
	
	// 비밀번호 찾기 (id, name, email)
	@Query(value="SELECT * FROM member WHERE id = :id AND name = :name AND email = :email", nativeQuery = true)
	Member findByIdAndNameAndEmail(String id, String name, String email);
	
	@Query(value="SELECT * FROM member WHERE id =:id", nativeQuery=true)
	Member findByLoginId(String id);
	
	// 닉네임 중복체크
	@Query(value="SELECT * FROM member WHERE nickname =:nickname", nativeQuery=true)
	Member findByNickname(String nickname);
	
	// 이메일 중복체크
	@Query(value="SELECT * FROM member WHERE email =:email", nativeQuery=true)
	Member findByEmail(String email);
	
	
	
	// 개인정보 수정
	@Transactional
	@Modifying
	@Query("UPDATE Member m SET m.pwd = :pwd WHERE m.id = :id")
	void updateMember(@Param("id") String id, @Param("pwd") String pwd);

	
	// 회원 탈퇴
	@Transactional
    @Modifying
    @Query("DELETE FROM Member m WHERE m.id = :id AND m.pwd = :pwd")
    void deleteMemberByIdAndPassword(@Param("id") String id, @Param("pwd") String pwd);
	@Modifying
    @Transactional
    @Query("UPDATE Member m SET m.withdrawalRequest = :withdrawalRequest WHERE m.id = :id AND m.pwd = :pwd")
    int updateWithdrawalRequest(@Param("id") String id, @Param("pwd") String pwd, @Param("withdrawalRequest") int withdrawalRequest);
	
}
