package com.project.logistic_management_2.repository.attached;

public interface AttachedImageRepoCustom {
    long deleteAttachedImages(String referenceId, String[] attachedImagePaths);
}
