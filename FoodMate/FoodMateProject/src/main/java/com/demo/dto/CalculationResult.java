package com.demo.dto;

public class CalculationResult {
    private double bmi;
    private double bmr;
    private double dailyCalories;
    private double dietCalories; // 다이어트 권장 칼로리
    private String obesity;

    public CalculationResult(double bmi, double bmr, double dailyCalories, double dietCalories) {
        this.bmi = bmi;
        this.bmr = bmr;
        this.dailyCalories = dailyCalories;
        this.dietCalories = dietCalories;
        setObesity(bmi); // BMI를 기준으로 비만도를 설정합니다.
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
        setObesity(bmi); // BMI가 변경될 때마다 비만도를 다시 설정합니다.
    }

    public double getBmr() {
        return bmr;
    }

    public void setBmr(double bmr) {
        this.bmr = bmr;
    }

    public double getDailyCalories() {
        return dailyCalories;
    }

    public void setDailyCalories(double dailyCalories) {
        this.dailyCalories = dailyCalories;
    }
    
    public double getDietCalories() {
        return dietCalories;
    }

    public void setDietCalories(double dietCalories) {
        this.dietCalories = dietCalories;
    }

    public String getObesity() {
        return obesity;
    }

    // BMI를 기준으로 비만도를 설정하는 메서드
    private void setObesity(double bmi) {
        if (bmi < 18.5) {
            this.obesity = "저체중";
        } else if (bmi >= 18.5 && bmi < 23) {
            this.obesity = "정상";
        } else if (bmi >= 23 && bmi < 25) {
            this.obesity = "과체중";
        } else if (bmi >= 25 && bmi < 30) {
            this.obesity = "비만";
        } else {
            this.obesity = "고도비만";
        }
    }
}
