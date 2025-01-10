package com.project.logistic_management_2.dto.attached;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AttachedImagePathsDTO {
    @NotNull(message = "Vui lòng cung cấp hình ảnh minh chứng!")
    @NotEmpty(message = "Vui lòng cung cấp ít nhất một ảnh minh chứng!")
    private List<String> attachedImagePaths;
}
