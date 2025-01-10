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
import java.rmi.ServerException;
import java.util.List;
import java.util.Objects;

@Service
public class FileStorageServiceImpl implements FileStoarageService {

    private final Path fileStorageLocation;

    public FileStorageServiceImpl(@Value("${file.upload-dir}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir)
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Không thể tạo thư mục lưu trữ file!", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            if(fileName.contains("..")) {
                throw new RuntimeException("Tên file không hợp lệ: " + fileName);
            }

            // Copy file đến vị trí lưu trữ
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Không thể lưu file " + fileName, ex);
//            throw new ServerException("Đã có lỗi xảy ra. Vui lòng thử lại sau!");
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

    public void deleteFiles(String[] fileNames) throws ServerException {
        for (String fileName : fileNames) {
            try {
                Path filePathToDelete = this.fileStorageLocation.resolve(fileName).normalize();
                boolean deleted = Files.deleteIfExists(filePathToDelete);
                if (deleted) {
                    System.out.println("Đã xoá file: " + fileName);
                } else {
                    System.out.println("Không tìm thấy file để xoá: " + fileName);
                }
            } catch (IOException ex) {
                System.err.println("Lỗi khi xoá file " + fileName + ":" +  ex.getMessage());
                throw new ServerException("Đã có lỗi xảy ra. Vui lòng thử lại sau!");
            }
        }
    }
}