package com.demo.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.dto.Com_Recipe;

public interface Com_RecipeRepository extends JpaRepository<Com_Recipe, Integer> {
	
	
		@Query(value="SELECT * FROM Com_Recipe c WHERE c.idx = ?1 ", nativeQuery=true)
		public Com_Recipe findCom_RecipeByIdx(int idx); 

}
