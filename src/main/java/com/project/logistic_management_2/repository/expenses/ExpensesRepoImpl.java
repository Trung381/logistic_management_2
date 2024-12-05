package com.project.logistic_management_2.repository.expenses;

import com.project.logistic_management_2.dto.expenses.ExpensesDTO;
import static com.project.logistic_management_2.entity.QExpenses.expenses;
import static com.project.logistic_management_2.entity.QExpensesConfig.expensesConfig;
import com.project.logistic_management_2.repository.BaseRepo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.JPAExpressions;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ExpensesRepoImpl extends BaseRepo implements ExpensesRepoCustom {
    public ExpensesRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    private ConstructorExpression<ExpensesDTO> expensesProjection() {
        return Projections.constructor(ExpensesDTO.class,
                expenses.id.as("id"),
                expenses.scheduleId.as("scheduleId"),
                expenses.expensesConfigId.as("expensesConfigId"),
                // Loại chi phí
                JPAExpressions.select(expensesConfig.type.as("expensesConfigType"))
                        .from(expensesConfig)
                        .where(expensesConfig.id.eq(expenses.expensesConfigId)),
                expenses.amount.as("amount"),
                expenses.note.as("note"),
                expenses.imgPath.as("imgPath"),
                expenses.status.as("status"),
                expenses.createdAt.as("createdAt"),
                expenses.updatedAt.as("updatedAt")
        );
    }

    @Override
    public List<ExpensesDTO> getAll() {
        return query.from(expenses)
                .where(expenses.deleted.eq(false))
                .select(expensesProjection())
                .fetch();
    }

    @Override
    public Optional<ExpensesDTO> getByID(String id) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(expenses.id.eq(id))
                .and(expenses.deleted.eq(false));

        return Optional.ofNullable(
                query.from(expenses)
                        .where(builder)
                        .select(expensesProjection())
                        .fetchOne()
        );
    }

    @Override
    @Modifying
    @Transactional
    public long delete(String id) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(expenses.id.eq(id))
                .and(expenses.deleted.eq(false));

        return query.update(expenses)
                .where(builder)
                .set(expenses.deleted, true)
                .execute();
    }

    @Override
    @Modifying
    @Transactional
    public long approve(String id) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(expenses.id.eq(id))
                .and(expenses.deleted.eq(false))
                .and(expenses.status.eq(0));

        return query.update(expenses)
                .where(builder)
                .set(expenses.status, 1)
                .execute();
    }
}
