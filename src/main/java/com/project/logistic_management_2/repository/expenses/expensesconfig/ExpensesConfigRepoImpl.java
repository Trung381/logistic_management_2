package com.project.logistic_management_2.repository.expenses.expensesconfig;

import com.project.logistic_management_2.dto.expenses.ExpensesConfigDTO;
import com.project.logistic_management_2.entity.ExpensesConfig;
import com.project.logistic_management_2.repository.BaseRepo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.project.logistic_management_2.entity.QExpensesConfig.expensesConfig;

@Repository
public class ExpensesConfigRepoImpl extends BaseRepo implements ExpensesConfigRepoCustom {
    public ExpensesConfigRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<ExpensesConfigDTO> getAll() {
        return query.from(expensesConfig)
                .where(expensesConfig.deleted.eq(false))
                .select(
                        Projections.fields(ExpensesConfigDTO.class,
                                expensesConfig.id.as("id"),
                                expensesConfig.type.as("type"),
                                expensesConfig.note.as("note"),
                                expensesConfig.createdAt.as("createdAt"),
                                expensesConfig.updatedAt.as("updatedAt"))
                )
                .fetch();
    }

    BooleanBuilder initBuilder(String id) {
        return new BooleanBuilder()
                .and(expensesConfig.id.eq(id))
                .and(expensesConfig.deleted.eq(false));
    }

    @Override
    public Optional<ExpensesConfig> getByID(String id) {
        BooleanBuilder builder = initBuilder(id);
        return Optional.ofNullable(
                query.from(expensesConfig)
                        .where(builder)
                        .select(expensesConfig)
                        .fetchOne()
        );
    }

    @Override
    @Modifying
    @Transactional
    public long delete(String id) {
        BooleanBuilder builder = initBuilder(id);
        return query.update(expensesConfig)
                .where(builder)
                .set(expensesConfig.deleted, true)
                .execute();
    }
}
