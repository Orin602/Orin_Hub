package com.demo.persistence;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo.domain.Com_Board_Detail;
import com.demo.domain.CommunityBoard;

public interface CommunityBoardRepository extends JpaRepository<CommunityBoard, Integer> {
	// 상세페이지 정보 출력
	@Query(value="SELECT * FROM Community_Board WHERE community_seq = ?1 ", nativeQuery=true)
	public CommunityBoard getCommunityBoard(int community_seq); 
	
	 //게시글 출력
	@Query(value="SELECT * FROM Community_Board ORDER BY community_seq DESC ", nativeQuery=true)
	public List<CommunityBoard> getBoard_List();
	
	// 제목으로 검색
	@Query(value="SELECT b.* FROM Community_Board b WHERE community_title LIKE %?1% ", nativeQuery=true)
	public Page<CommunityBoard> findCommunityBoardByTitleContainingOrderByTitle(String title, int community_seq, Pageable pageable);
	
	// 글쓴이 아이디로 검색
	@Query(value="SELECT b.community_seq, b.no_data, b.community_title, b.community_content, b.community_d_regdate, b.community_cnt, b.community_goodpoint FROM Community_Board b JOIN "
			+ " Member_Data m ON b.no_data = m.no_data WHERE m.id = ?1 ", nativeQuery=true)
	public Page<CommunityBoard> findCommunityBoardByIdContainingOrderById(String id, int community_seq, Pageable pageable);
	
	//전체글 페이징처리 
	@Query(value="SELECT * FROM Community_Board ORDER BY community_seq DESC ", nativeQuery=true)
	public Page<CommunityBoard> findAllCommunityBoard(int community_seq, Pageable pageable);
	
	// 마이페이지
    // 회원별 작성한 게시글(레시피)조회
    @Query("SELECT b FROM CommunityBoard b JOIN b.member_data m WHERE m.id=:id")
    public List<CommunityBoard> getMyRecipeListById(@Param("id")String id);
    
    //pdf
    @Query(value="SELECT * FROM Community_Board WHERE community_seq = ?1 ", nativeQuery=true)
    public CommunityBoard findCommunityBoardByCommunity_seq(String community_seq);
    
}
