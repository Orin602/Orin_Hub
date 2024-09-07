package com.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RecipeController {

    @GetMapping("/Top_recipes")
    public String showTopRecipesPage() {
        return "Top_recipes";
    }
}