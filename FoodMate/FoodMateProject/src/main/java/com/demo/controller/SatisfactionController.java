package com.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.domain.SatisfactionSurvey;
import com.demo.service.SatisfactionService;

@Controller
public class SatisfactionController {

    @Autowired
    private SatisfactionService satisfactionService;

    @PostMapping("/submitSatisfaction")
    @ResponseBody
    public String submitSatisfaction(@RequestBody SatisfactionRequest satisfactionRequest) {
        SatisfactionSurvey satisfactionSurvey = new SatisfactionSurvey();
        
        switch(satisfactionRequest.getSatisfaction()) {
            case "very-satisfied":
                satisfactionSurvey.setVerySatisfied(1);
                break;
            case "satisfied":
                satisfactionSurvey.setSatisfied(1);
                break;
            case "neutral":
                satisfactionSurvey.setNeutral(1);
                break;
            case "dissatisfied":
                satisfactionSurvey.setDissatisfied(1);
                break;
            case "very-dissatisfied":
                satisfactionSurvey.setVeryDissatisfied(1);
                break;
        }

        satisfactionService.saveSatisfaction(satisfactionSurvey);

        return "{\"status\":\"success\"}";
    }

    @GetMapping("/admin/satisfactionResults")
    public String getSatisfactionResults(Model model) {
        List<SatisfactionSurvey> surveys = satisfactionService.getAllSatisfactionSurveys();
        model.addAttribute("surveys", surveys);
        return "admin/satisfactionResults";
    }
}

class SatisfactionRequest {
    private String satisfaction;

    // getter and setter
    public String getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(String satisfaction) {
        this.satisfaction = satisfaction;
    }
}
