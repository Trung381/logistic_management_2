package com.project.logistic_management_2.service.upload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir)
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
            System.out.println("Thư mục lưu trữ file: " + this.fileStorageLocation.toString());
        } catch (Exception ex) {
            throw new RuntimeException("Không thể tạo thư mục lưu trữ file!", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        // Lấy tên file gốc
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Kiểm tra tên file hợp lệ
            if(fileName.contains("..")) {
                throw new RuntimeException("Tên file không hợp lệ: " + fileName);
            }

            // Copy file đến vị trí lưu trữ
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Không thể lưu file " + fileName, ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File không tồn tại " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File không tồn tại " + fileName, ex);
        }
    }
}
