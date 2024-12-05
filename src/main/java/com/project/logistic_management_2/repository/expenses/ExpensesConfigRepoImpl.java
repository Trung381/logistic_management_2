package com.project.logistic_management_2.repository.expenses;

import com.project.logistic_management_2.dto.expenses.ExpensesConfigDTO;
import com.project.logistic_management_2.entity.ExpensesConfig;
import com.project.logistic_management_2.entity.QExpensesConfig;
import com.project.logistic_management_2.repository.BaseRepo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ExpensesConfigRepoImpl extends BaseRepo implements ExpensesConfigRepoCustom {
    public ExpensesConfigRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<ExpensesConfigDTO> getAll() {
        QExpensesConfig qExpensesConfig = QExpensesConfig.expensesConfig;
        return query.from(qExpensesConfig)
                .where(qExpensesConfig.deleted.eq(false))
                .select(
                        Projections.fields(ExpensesConfigDTO.class,
                                qExpensesConfig.id.as("id"),
                                qExpensesConfig.type.as("type"),
                                qExpensesConfig.note.as("note"),
                                qExpensesConfig.createdAt.as("createdAt"),
                                qExpensesConfig.updatedAt.as("updatedAt"))
                )
                .fetch();
    }

    @Override
    public Optional<ExpensesConfig> getByID(String id) {
        QExpensesConfig qExpensesConfig = QExpensesConfig.expensesConfig;

        BooleanBuilder builder = new BooleanBuilder()
                .and(qExpensesConfig.id.eq(id))
                .and(qExpensesConfig.deleted.eq(false));

        return Optional.ofNullable(
                query.from(qExpensesConfig)
                        .where(builder)
                        .select(qExpensesConfig)
                        .fetchOne()
        );
    }

    @Override
    @Modifying
    @Transactional
    public long delete(String id) {
        QExpensesConfig qExpensesConfig = QExpensesConfig.expensesConfig;

        BooleanBuilder builder = new BooleanBuilder()
                .and(qExpensesConfig.id.eq(id))
                .and(qExpensesConfig.deleted.eq(false));

        return query.update(qExpensesConfig)
                .where(builder)
                .set(qExpensesConfig.deleted, true)
                .execute();
    }
}
