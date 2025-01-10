package com.project.logistic_management_2.controller.attached;

import com.project.logistic_management_2.dto.BaseResponse;
import com.project.logistic_management_2.dto.attached.AttachedImagePathsDTO;
import com.project.logistic_management_2.enums.attached.AttachedType;
import com.project.logistic_management_2.service.attached.AttachedImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.ServerException;

@RestController
@RequestMapping("/attached_images")
@RequiredArgsConstructor
public class AttachedImageController {
    private final AttachedImageService attachedImageService;

    @PostMapping("/{refId}")
    public ResponseEntity<Object> attachImages(@PathVariable String refId, @RequestParam AttachedType type, @Valid @RequestBody AttachedImagePathsDTO attachedImagePathsDTO) {
        return ResponseEntity.ok(
                BaseResponse.ok(attachedImageService.addAttachedImages(refId, type, attachedImagePathsDTO))
        );
    }

    @PostMapping("/delete/{refId}")
    public ResponseEntity<Object> deleteAttachedImages(@PathVariable String refId, @RequestParam AttachedImagePathsDTO attachedImagePathsDTO) throws ServerException {
        String result = attachedImageService.deleteAttachedImages(refId, attachedImagePathsDTO);
        return ResponseEntity.ok(BaseResponse.ok(
                        result,
                        "Đã xóa thành công " + result + " ảnh minh chứng!")
        );
    }
}
