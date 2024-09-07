package com.demo.service;

import java.io.IOException;

import org.springframework.stereotype.Component;

@Component
public class PythonScriptRunner {

    public void runPythonScript() {
        try {
            // Python 스크립트 실행
            Process process = Runtime.getRuntime().exec("python E:/Student/MachineLearning/autoUpdate.py");
            int exitCode = process.waitFor();

            // 실행 결과 처리
            if (exitCode == 0) {
                System.out.println("Python script executed successfully.");
            } else {
                System.err.println("Error: Python script execution failed.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
