package com.project.logistic_management_2.service.attached;

import com.project.logistic_management_2.dto.attached.AttachedImagePathsDTO;
import com.project.logistic_management_2.entity.attached.AttachedImage;
import com.project.logistic_management_2.enums.attached.AttachedType;
import com.project.logistic_management_2.exception.define.NotFoundException;
import com.project.logistic_management_2.mapper.attached.AttachedImageMapper;
import com.project.logistic_management_2.repository.attached.AttachedImageRepo;
import com.project.logistic_management_2.service.upload.FileStorageServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.rmi.ServerException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttachedImageServiceImpl implements AttachedImageService{
    private final AttachedImageRepo attachedRepo;
    private final AttachedImageMapper attachedMapper;
    private final FileStorageServiceImpl fileStorageService;

    @Override
    public List<AttachedImage> addAttachedImages(String referenceId, AttachedType type, AttachedImagePathsDTO attachedImagePathsDTO) {
        String[] attachedImagePaths = attachedImagePathsDTO.getAttachedImagePaths();
        return addAttachedImages(referenceId, type, attachedImagePaths);
    }

    public List<AttachedImage> addAttachedImages(String referenceId, AttachedType type, String[] attachedImagePaths) {
        List<AttachedImage> attachedImages = attachedMapper.toAttachedImages(referenceId, type, attachedImagePaths);
        return attachedRepo.saveAll(attachedImages);
    }

    @Override
    @Transactional
    public String deleteAttachedImages(String referenceId, AttachedImagePathsDTO attachedImagePathsDTO) throws ServerException {
        String[] attachedImagePaths = attachedImagePathsDTO.getAttachedImagePaths();
        long numOfRowsDeleted = attachedRepo.deleteAttachedImages(referenceId, attachedImagePaths);
        if (numOfRowsDeleted == 0) {
            throw new NotFoundException("Ảnh minh chứng không tồn tại!");
        }
        fileStorageService.deleteFiles(attachedImagePaths);
        return numOfRowsDeleted + "/" + attachedImagePaths.length;
    }
}
