package com.demo.persistence;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.domain.GoCamping;

@Repository
public interface GoCampingRepository extends JpaRepository<GoCamping, Integer> {

    @Query("SELECT g.doNm FROM GoCamping g")
    List<String> findAllDoNmList();
    
    @Query("SELECT g.facltDivNm FROM GoCamping g")
    List<String> findAllFacltDivList();
    
    @Query("SELECT g.lctCl FROM GoCamping g")
	List<String> findAllLctClList();
    
    @Query("SELECT g.induty FROM GoCamping g")
	List<String> findAllIndutyList();
    
    
    @Query("SELECT g.sbrsCl FROM GoCamping g")
	List<String> findAllSbrsClList();
    
    @Query(value = "SELECT * FROM go_camping g WHERE (:doNm IS NULL OR :doNm = '' OR g.do_nm LIKE :doNm)", nativeQuery = true)
    List<GoCamping> findByDoNmLike(@Param("doNm") String doNm);

    @Query(value = "SELECT * FROM go_camping g WHERE (:sigunguNm IS NULL OR :sigunguNm = '' OR g.sigungu_nm LIKE :sigunguNm)", nativeQuery = true)
    List<GoCamping> findBySigunguNmLike(@Param("sigunguNm") String sigunguNm);

    @Query(value = "SELECT * FROM go_camping g WHERE (:lctCl IS NULL OR :lctCl = '' OR g.lct_cl LIKE :lctCl)", nativeQuery = true)
    List<GoCamping> findByLctClLike(@Param("lctCl") String lctCl);

    @Query(value = "SELECT * FROM go_camping g WHERE (:faclt IS NULL OR :faclt = '' OR g.faclt_div_nm LIKE :faclt)", nativeQuery = true)
    List<GoCamping> findByFacltLike(@Param("faclt") String faclt);

    @Query(value = "SELECT * FROM go_camping g WHERE (:induty IS NULL OR :induty = '' OR g.induty LIKE :induty)", nativeQuery = true)
    List<GoCamping> findByIndutyLike(@Param("induty") String induty);

    @Query(value = "SELECT * FROM go_camping g WHERE (:sbrsCl IS NULL OR :sbrsCl = '' OR g.sbrs_cl LIKE :sbrsCl)", nativeQuery = true)
    List<GoCamping> findBySbrsClLike(@Param("sbrsCl") String sbrsCl);
    
    @Query(value = "SELECT * FROM go_camping g " +
            "WHERE (:keyword IS NULL OR :keyword = '' " +
            "OR g.faclt_nm LIKE %:keyword% " +
            "OR g.manage_sttus LIKE %:keyword% " +
            "OR g.induty LIKE %:keyword% " +
            "OR g.lct_cl LIKE %:keyword% " +
            "OR g.do_nm LIKE %:keyword% " +
            "OR g.sigungu_nm LIKE %:keyword% " +
            "OR g.oper_pd_cl LIKE %:keyword% " +
            "OR g.sbrs_cl LIKE %:keyword% " +
            "OR g.faclt_div_nm LIKE %:keyword% " +
            "OR g.addr1 LIKE %:keyword% " +
            "OR g.tel LIKE %:keyword% " +
            "OR g.homepage LIKE %:keyword% " +
            "OR g.resve_url LIKE %:keyword% " +
            "OR g.resve_cl LIKE %:keyword% " +
            "OR g.thema_envrn_cl LIKE %:keyword% " +
            "OR g.eqpmn_lend_cl LIKE %:keyword% )", nativeQuery = true)
List<GoCamping> findByKeywordInAllColumns(@Param("keyword") String keyword);

    @Query(value = "SELECT * FROM go_camping WHERE FACLT_NM LIKE %?1% " , nativeQuery = true)
    Page<GoCamping> searchreview_List(String FACLT_NM, int content_id, Pageable pageable);

    
    @Query(value = "SELECT * FROM go_camping g WHERE (:bottom IS NULL OR :bottom = '' " +
            "OR ('잔디' LIKE :bottom AND g.site_bottom_cl1 > 0) " +
            "OR ('파쇄석' LIKE :bottom AND g.site_bottom_cl2 > 0) " +
            "OR ('데크' LIKE :bottom AND g.site_bottom_cl3 > 0) " +
            "OR ('자갈' LIKE :bottom AND g.site_bottom_cl4 > 0) " +
            "OR ('맨흙' LIKE :bottom AND g.site_bottom_cl5 > 0))", nativeQuery = true)
    List<GoCamping> findByBottomLike(@Param("bottom") String bottom);
    
    
}
