package com.demo.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.domain.askBoard;

// Spring Data JPA 리포지토리 인터페이스로, 'askBoard' 엔티티와 관련된
// 데이터베이스 액세스를 제공.
// 이 인터페이스는 'JpaRepository'를 확장하여 기본적은 CRUD 작업을 자동으로 제공
// 또한, 여러 개의 커스텀 쿼리를 정의하여 특정 조건에 맞는 데이터를 검색할 수 있도록 함.

// Spring이 이 인터페이스를 리포지토리로 인식하게 하여 데이터 접근 계층의 빈으로 등록
@Repository 
// CustomerServiceRepository': 'JpaRepository<askBoard, Long>'를 상속받아
// 'askBoard' 엔티티에 대한 CRUD 작업을 제공
// 두 번째 파라미터는 엔티티의 ID 타입을 나타냄
public interface CustomerServiceRepository extends JpaRepository<askBoard, Long> {

	// CustomerServiceRepository: 고객 서비스와 관련된 데이터 액세스를 위한 리포지토리 인터페이스

    // Named 쿼리 사용 예시
	// Named 쿼리를 사용하여 주어진 주제에 따라 askBoard 엔티티를 검색
	// '@Query': 이 어노테이션은 커스텀 SQL 쿼리를 정의
	// 'nativeQuery = true' 는 이 쿼리가 네이티브 SQL 쿼리임을 나타냄
	// '@Param("subject")' : 쿼리에서 'subject' 파라미터를 설정
    @Query(value="SELECT * FROM ask_board WHERE subject = :subject", nativeQuery = true)
    List<askBoard> findBySubjectNamedQuery(@Param("subject") String subject);
    
    // Named 쿼리를 사용하여 주어진 이름에 따라 askBoard 엔티티를 검색
 // '@Param("name")' : 쿼리에서 'name' 파라미터를 설정
    @Query(value="SELECT * FROM ask_board WHERE name = :name", nativeQuery = true)
    List<askBoard> findByNameNamedQuery(@Param("name") String name);
	
    // 모든 askBoard 엔티티를 등록일(regdate)을 기준으로 정렬하여 검색
    @Query(value="SELECT * FROM ask_board ORDER BY regdate", nativeQuery = true)
    List<askBoard> findAll();

    // '@Query' : JPQL (Java Persistence Query Language) 쿼리를 사용하여 모든 askBoard 엔티티를 등록일(regdate)을 기준으로 정렬하여 검색
    @Query("SELECT a FROM askBoard a ORDER BY a.regdate")
    List<askBoard> getInquiryList();
    
    
    
    
    
    
}
