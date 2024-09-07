package com.demo.service;

import com.demo.dto.CalculationResult;
import com.demo.domain.MemberData;

public interface CalculatorService {
    CalculationResult calculate(MemberData memberData);
    
}
