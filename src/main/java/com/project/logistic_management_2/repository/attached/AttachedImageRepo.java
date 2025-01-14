package com.project.logistic_management_2.repository.attached;

import com.project.logistic_management_2.entity.attached.AttachedImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachedImageRepo extends JpaRepository<AttachedImage, Integer>, AttachedImageRepoCustom {
}
