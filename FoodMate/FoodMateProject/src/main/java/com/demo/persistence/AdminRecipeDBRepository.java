package com.demo.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.domain.foodRecipe;
import com.demo.dto.RecommendData;

public interface AdminRecipeDBRepository extends JpaRepository<foodRecipe, Long> {
	
	@Query(value = "SELECT * FROM food_recipe WHERE name LIKE %:searchWord% ORDER BY idx DESC", nativeQuery = true)
	List<foodRecipe> getRecipeDBListByName(String searchWord);
	
	@Query(value = "SELECT * FROM food_recipe WHERE LOWER(recipeingredientparts) LIKE LOWER('%' || :searchInput || '%')", nativeQuery = true)
    List<foodRecipe> getRecipeDBListByRecipeingredientparts(String searchInput);
	
	@Query(value = "SELECT * FROM food_recipe WHERE idx LIKE :searchWord ORDER BY idx DESC", nativeQuery = true)
	List<foodRecipe> getRecipeDBListByIndex(String searchWord);
	
	@Query(value = "SELECT IMAGES FROM food_recipe WHERE IDX = :idx", nativeQuery = true)
	String getImagesByIndex(int idx);
	
	@Query(value = "SELECT * FROM food_recipe WHERE idx = :searchWord", nativeQuery = true)
	foodRecipe getRecipeDBByIndex(long searchWord);
	
	@Query(value = "SELECT * FROM food_recipe WHERE idx = :idx", nativeQuery = true)
	foodRecipe getFoodByIndex(int idx);
	
	@Query(value = "SELECT count(*) FROM food_recipe", nativeQuery = true)
	int getRecipeCount();
}
