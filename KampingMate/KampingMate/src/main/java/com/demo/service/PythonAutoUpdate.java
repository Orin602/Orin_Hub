package com.demo.service;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PythonAutoUpdate implements CommandLineRunner {

    private final PythonScriptRunner pythonScriptRunner;

    public PythonAutoUpdate(PythonScriptRunner pythonScriptRunner) {
        this.pythonScriptRunner = pythonScriptRunner;
    }

    @Override
    public void run(String... args) throws Exception {
        // 애플리케이션 시작시 Python 스크립트 실행
        pythonScriptRunner.runPythonScript();
    }
}