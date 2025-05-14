package com.demo.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.domain.Store;

public interface StoreRepository extends JpaRepository<Store, Integer> {

	// 회원 ID로 구매 내역 조회
	List<Store> findByMemberId(String member);

	Store findByStoreseq(int storeseq);

}
