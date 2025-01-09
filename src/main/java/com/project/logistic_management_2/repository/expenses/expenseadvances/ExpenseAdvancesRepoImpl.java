package com.project.logistic_management_2.repository.expenses.expenseadvances;

import com.project.logistic_management_2.dto.expenses.ExpenseAdvancesDTO;
import com.project.logistic_management_2.enums.Pagination;
import com.project.logistic_management_2.repository.BaseRepo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.project.logistic_management_2.entity.QExpenseAdvances.expenseAdvances;
import static com.project.logistic_management_2.entity.QUser.user;

@Repository
public class ExpenseAdvancesRepoImpl extends BaseRepo implements ExpenseAdvancesRepoCustom {
    public ExpenseAdvancesRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    private ConstructorExpression<ExpenseAdvancesDTO> constructorExpression() {
        return Projections.constructor(ExpenseAdvancesDTO.class,
                expenseAdvances.id.as("id"),
                expenseAdvances.driverId.as("driverId"),
                JPAExpressions.select(user.fullName.as("driverName"))
                        .from(user)
                        .where(expenseAdvances.driverId.eq(user.id)),
                expenseAdvances.period.as("period"),
                expenseAdvances.advance.as("advance"),
                expenseAdvances.remainingBalance.coalesce(0f).as("remainingBalance"),
                expenseAdvances.note.coalesce("").as("note"),
                expenseAdvances.createdAt.as("createdAt"),
                expenseAdvances.updatedAt.as("updatedAt")
        );
    }

    @Override
    public List<ExpenseAdvancesDTO> getAll(int page) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(expenseAdvances.deleted.eq(false));

        long offset = (long) (page - 1) * Pagination.TEN.getSize();
        return query.from(expenseAdvances)
                .where(builder)
                .select(constructorExpression())
                .offset(offset)
                .limit(Pagination.TEN.getSize())
                .fetch();
    }

    @Override
    public Optional<ExpenseAdvancesDTO> getByDriverId(String id) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(expenseAdvances.deleted.eq(false))
                .and(expenseAdvances.driverId.eq(id));

        return Optional.ofNullable(
                query.from(expenseAdvances)
                        .where(builder)
                        .select(constructorExpression())
                        .fetchOne()
        );
    }

    @Override
    @Modifying
    @Transactional
    public long deleted(Integer id) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(expenseAdvances.deleted.eq(false))
                .and(expenseAdvances.id.eq(id));

        return query.update(expenseAdvances)
                .where(builder)
                .set(expenseAdvances.deleted, true)
                .execute();
    }

    @Override
    public Optional<ExpenseAdvancesDTO> getExpenseAdvanceById(Integer id) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(expenseAdvances.deleted.eq(false))
                .and(expenseAdvances.id.eq(id));

        return Optional.ofNullable(
                query.from(expenseAdvances)
                        .where(builder)
                        .select(constructorExpression())
                        .fetchOne()
        );
    }
}
