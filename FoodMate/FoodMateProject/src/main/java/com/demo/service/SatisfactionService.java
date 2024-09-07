package com.demo.service;

import java.util.List;

import com.demo.domain.SatisfactionSurvey;

public interface SatisfactionService {
    void saveSatisfaction(SatisfactionSurvey satisfactionSurvey);
    List<SatisfactionSurvey> getAllSatisfactionSurveys();
}
