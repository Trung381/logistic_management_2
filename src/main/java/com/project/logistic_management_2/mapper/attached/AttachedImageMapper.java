package com.project.logistic_management_2.mapper.attached;

import com.project.logistic_management_2.entity.AttachedImage;
import com.project.logistic_management_2.enums.attached.AttachedType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class AttachedImageMapper {
    public List<AttachedImage> toAttachedImages(String referenceId, AttachedType type, List<String> attachedImagePaths) {
        List<AttachedImage> images = new ArrayList<>();
        for (String path : attachedImagePaths) {
            images.add(
                    crateImagePath(referenceId, type, path.trim())
            );
        }
        return images;
    }

    public List<AttachedImage> toAttachedImages(String referenceId, AttachedType type, String[] attachedImagePaths) {
        List<AttachedImage> images = new ArrayList<>();
        for (String path : attachedImagePaths) {
            images.add(
                    crateImagePath(referenceId, type, path.trim())
            );
        }
        return images;
    }

    private AttachedImage crateImagePath(String referenceId, AttachedType type, String path) {
        return AttachedImage.builder()
                .referenceId(referenceId)
                .type(type.getValue())
                .imgPath(path.trim())
                .uploadedAt(new Date())
                .build();
    }
}
