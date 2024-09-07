package com.demo.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.domain.SatisfactionSurvey;

public interface SatisfactionRepository extends JpaRepository<SatisfactionSurvey, Long> {

	@Query(value="SELECT * FROM satisfaction_survey ORDER BY id DESC", nativeQuery = true)
	List<SatisfactionSurvey> getAllSatisfactionSurveys();
	
	@Query(value="SELECT COUNT(*) FROM satisfaction_survey WHERE VERY_SATISFIED = 1 or SATISFIED = 1", nativeQuery = true)
	int getSatisFiedCount();
	
	@Query(value="SELECT COUNT(*) FROM satisfaction_survey", nativeQuery = true)
	int getAllCount();
}