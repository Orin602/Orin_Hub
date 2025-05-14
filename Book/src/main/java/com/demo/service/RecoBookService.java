package com.demo.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.domain.RecoBook;
import com.demo.persistence.RecoBookRepository;

@Service
public class RecoBookService {

	@Autowired
	private RecoBookRepository recoBookRepo;
	
	@Transactional
	public void saveRecoBook(List<RecoBook> recommend) {
		for(RecoBook recoBook : recommend) {
			recoBook.setRecommendationDate(new Date());
			recoBookRepo.save(recoBook);
		}
	}
	
	public List<RecoBook> recoBookHistory(String memberId) {
		return recoBookRepo.findByMemberId(memberId);
	}
}
