//package com.project.logistic_management_2.controller.upload;
//
//import com.project.logistic_management_2.service.upload.FileStorageServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.Resource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/files")
//public class FileController {
//
//    @Autowired
//    private FileStorageServiceImpl fileStorageService;
//
//    // API Upload Multiple Files
//    @PostMapping("/upload/multiple")
//    public ResponseEntity<Map<String, List<String>>> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
//        List<String> uploadedFileNames = new ArrayList<>();
//        for (MultipartFile file : files) {
//            String fileName = fileStorageService.storeFile(file);
//            uploadedFileNames.add(fileName);
//        }
//        Map<String, List<String>> response = new HashMap<>();
//        response.put("attachedImagePaths", uploadedFileNames);
//        return ResponseEntity.ok(response);
//    }
//
//    // API Upload File
//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
//        String fileName = fileStorageService.storeFile(file);
//        return ResponseEntity.ok("/uploads/" + fileName);
//    }
//
//    // API Download File
//    @GetMapping("/download/{fileName:.+}")
//    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
//        Resource resource = fileStorageService.loadFileAsResource(fileName);
//
//        String contentType = "application/octet-stream";
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
//                .body(resource);
//    }
//
//    @GetMapping("/view/{fileName:.+}")
//    public ResponseEntity<Resource> viewFile(@PathVariable String fileName) {
//        Resource resource = fileStorageService.loadFileAsResource(fileName);
//
//        String contentType = "application/octet-stream";
//        try {
//            contentType = Files.probeContentType(resource.getFile().toPath());
//        } catch (IOException ex) {
//            System.out.println("Không thể xác định loại MIME cho file: " + fileName);
//        }
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_TYPE, contentType)
//                .body(resource);
//    }
//
//    @PostMapping("/delete/multiple")
//    public ResponseEntity<String> deleteMultipleFiles(@RequestBody List<String> fileNames) {
//        fileStorageService.deleteFiles(fileNames);
//        return new ResponseEntity<>("Đã xoá các file được yêu cầu.", HttpStatus.OK);
//    }
//}
package com.project.logistic_management_2.controller.upload;

import com.project.logistic_management_2.service.upload.FileStorageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileStorageServiceImpl fileStorageService;

    // API Upload Multiple Files
    @PostMapping("/upload/multiple")
    public ResponseEntity<Map<String, String[]>> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return ResponseEntity.ok(fileStorageService.storeFiles(files));
    }

    // API Upload File
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(fileStorageService.storeFileAndReturnResponse(file));
    }

    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        String contentType = "application/octet-stream";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/view/{fileName:.+}")
    public ResponseEntity<Resource> viewFile(@PathVariable String fileName) {
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        String contentType = "application/octet-stream";
        try {
            contentType = Files.probeContentType(resource.getFile().toPath());
        } catch (IOException ex) {
            System.out.println("Không thể xác định loại MIME cho file: " + fileName);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(resource);
    }

    @PostMapping("/delete/multiple")
    public ResponseEntity<String> deleteMultipleFiles(@RequestBody String[] fileNames) {
        fileStorageService.deleteFiles(fileNames);
        return ResponseEntity.ok("Đã xoá các file được yêu cầu.");
    }
}