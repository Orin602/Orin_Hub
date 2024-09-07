package com.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.domain.SatisfactionSurvey;
import com.demo.persistence.SatisfactionRepository;

@Service
public class SatisfactionServiceImpl implements SatisfactionService {

    @Autowired
    private SatisfactionRepository satisfactionRepository;

    @Override
    public void saveSatisfaction(SatisfactionSurvey satisfactionSurvey) {
        satisfactionRepository.save(satisfactionSurvey);
    }
    
    @Override
    public List<SatisfactionSurvey> getAllSatisfactionSurveys() {
        return satisfactionRepository.getAllSatisfactionSurveys();
    }
}
