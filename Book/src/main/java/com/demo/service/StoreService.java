package com.demo.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.domain.OrderStatus;
import com.demo.domain.Store;
import com.demo.persistence.StoreRepository;

import jakarta.transaction.Transactional;

@Service
public class StoreService {

	@Autowired
	private StoreRepository storeRepo;
	
	// 구매 내역 저장
	public Store insertOrder(Store store) {
		// 기본 상태는 PENDING
        if (store.getStatus() == null) {
            store.setStatus(OrderStatus.PENDING);
        }
		store.setOrderDate(new Date());
		return storeRepo.save(store);
	}
	
	// 구매 상태 수정(관리자)
	@Transactional
	public Store updateStatus(int id, OrderStatus status) throws IllegalAccessException {
		Store updateOrder = storeRepo.findById(id)
				.orElseThrow(() -> new IllegalAccessException("해당 주문 내역이 없습니다. id:" + id));
		if (updateOrder.getStatus() == OrderStatus.COMPLETED) {
	        throw new IllegalAccessException("완료된 주문은 상태를 변경할 수 없습니다.");
	    }
		updateOrder.setStatus(status);
		
		return storeRepo.save(updateOrder);
	}
	// 구매 내역 수정
	@Transactional
	public Store updateOrder(int id, Store vo) throws IllegalAccessException {
		Store updateOrder = storeRepo.findById(id)
				.orElseThrow(() -> new IllegalAccessException("해당 주문 내역이 없습니다. id:" + id));
		
		updateOrder.setEA(vo.getEA());
		updateOrder.setPrice(vo.getPrice());
		
		return storeRepo.save(updateOrder);
	}

	// 구매 내역 조회(회원 전체)
	public List<Store> getAllOrder() {
		return storeRepo.findAll();
	}
	// 구매 내역 조회(특정 회원)
	public List<Store> getMyOrder(String id) {
		return storeRepo.findByMemberId(id);
	}
	public Store getStoreBySeq(int storeseq) {
		return storeRepo.findByStoreseq(storeseq);
	}
	
	// 구매 내역 삭제
	public void deleteOrder(int id) {
		storeRepo.deleteById(id);
	}
}
