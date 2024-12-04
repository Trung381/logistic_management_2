package com.project.logistic_management_2.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public class BaseRepo {
    protected EntityManager entityManager;
    protected JPAQueryFactory query;

    public BaseRepo(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.query = new JPAQueryFactory(entityManager);
    }
}
