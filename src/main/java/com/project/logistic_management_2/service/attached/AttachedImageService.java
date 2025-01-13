package com.project.logistic_management_2.service.attached;

import com.project.logistic_management_2.dto.attached.AttachedImagePathsDTO;
import com.project.logistic_management_2.entity.AttachedImage;
import com.project.logistic_management_2.enums.attached.AttachedType;

import java.rmi.ServerException;
import java.util.List;

public interface AttachedImageService {
    List<AttachedImage> addAttachedImages(String referenceId, AttachedType type, AttachedImagePathsDTO attachedImagePathsDTO);
    List<AttachedImage> addAttachedImages(String referenceId, AttachedType type, String[] attachedImagePaths);
    String deleteAttachedImages(String referenceId, AttachedImagePathsDTO attachedImagePathsDTO) throws ServerException;
}
