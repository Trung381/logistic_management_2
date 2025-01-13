package com.project.logistic_management_2.dto.attached;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class AttachedImagePathsDTO {
    @NotEmpty(message = "Vui lòng cung cấp ít nhất một ảnh minh chứng!")
    private String[] attachedImagePaths;
}
