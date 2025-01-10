package com.project.logistic_management_2.repository.attached;

import com.project.logistic_management_2.repository.BaseRepo;
import com.querydsl.core.BooleanBuilder;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import static com.project.logistic_management_2.entity.QAttachedImage.attachedImage;

@Repository
public class AttachedImageRepoImpl extends BaseRepo implements AttachedImageRepoCustom {

    public AttachedImageRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    @Transactional
    @Modifying
    public long deleteAttachedImages(String referenceId, String[] attachedImagePaths) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(attachedImage.referenceId.eq(referenceId))
                .and(attachedImage.imgPath.in(attachedImagePaths));
        return query.delete(attachedImage)
                .where(builder)
                .execute();
    }
}
