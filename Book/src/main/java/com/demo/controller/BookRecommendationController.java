package com.demo.controller;

import com.demo.dto.BookRecommendation;
import com.demo.service.BookRecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.xml.bind.JAXBException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BookRecommendationController {

    private final BookRecommendationService bookRecommendationService;

    public BookRecommendationController(BookRecommendationService bookRecommendationService) {
        this.bookRecommendationService = bookRecommendationService;
    }

    @GetMapping("/recommendations")
    public ResponseEntity<?> getRecommendations() {
        try {
            List<BookRecommendation> recommendations = bookRecommendationService.getBookRecommendations();
            return ResponseEntity.ok(recommendations);
        } catch (JAXBException e) {
            // Handle the exception and return an error response
            return ResponseEntity.status(500).body("Failed to fetch book recommendations.");
        } catch (Exception e) {
            // Handle other exceptions
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }
}
