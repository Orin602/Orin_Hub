package com.demo.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.dto.Foodcal;

public interface FoodcalRepository extends JpaRepository<Foodcal, String> {
	Optional<Foodcal> findByFoodname(String foodName);
}
