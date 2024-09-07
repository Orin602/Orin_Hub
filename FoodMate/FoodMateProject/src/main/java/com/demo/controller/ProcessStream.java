package com.demo.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ProcessStream implements Runnable {
	
	private static final String UTF_8 = "utf-8";
	private String name;
	private InputStream inputStream;
	private Thread thread;
	StringBuilder sb = new StringBuilder();
	
	public ProcessStream(String name, InputStream inputStream) {
		this.name = name;
		this.inputStream = inputStream;
	}
	
	public String start() throws InterruptedException {
		this.thread = new Thread(this);
		this.thread.start();	
		this.thread.join();
		
		return sb.toString();
	}
	
	@Override
	public void run() {
		InputStreamReader isr = null;
		BufferedReader br = null;
		String lines = "";
		
		try {
			isr = new InputStreamReader(inputStream, UTF_8);
			br = new BufferedReader(isr);
			
			while(true) {
				String line = br.readLine();
				// 삭제
				System.out.println(line);
				if (line == null)
					break;
				
				lines += line;
				lines += "\n";
			}
			
			if (!lines.equals("")) {
//				System.out.println("[" + name + "]");
//				System.out.println(lines);
				sb.append(lines);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				
				if (inputStream != null)
					inputStream.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getResult() {
		
		return sb.toString();
	}
}










