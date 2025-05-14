package com.demo.domain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {

	public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
		Path uploadPath = Paths.get(uploadDir);

		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}

		try {
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(multipartFile.getInputStream(), filePath);
		} catch (IOException e) {
			throw new IOException("Could not save file: " + fileName, e);
		}
	}

	// 이미지 삭제 메서드
	public static void deleteFile(String filePath) throws IOException {
		Path path = Paths.get(filePath);
		Files.deleteIfExists(path);
	}
}
