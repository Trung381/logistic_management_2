package com.project.logistic_management_2.repository.transaction;

import com.project.logistic_management_2.dto.request.TransactionDTO;
import com.project.logistic_management_2.dto.transaction.UpdateTransactionDTO;
import com.project.logistic_management_2.entity.Transaction;
import com.project.logistic_management_2.exception.def.EditNotAllowedException;
import com.project.logistic_management_2.repository.BaseRepo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAUpdateClause;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;


import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import static com.project.logistic_management_2.entity.QGoods.goods;
import static com.project.logistic_management_2.entity.QTransaction.transaction;
import static com.project.logistic_management_2.entity.QUser.user;

@Repository
public class TransactionRepoImpl extends BaseRepo implements TransactionRepoCustom {
    public TransactionRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    private ConstructorExpression<TransactionDTO> transactionProjection() {

        return Projections.constructor(TransactionDTO.class,
                transaction.id.as("id"),
                transaction.refUserId.as("refUserId"),
                JPAExpressions.select(user.fullName.as("fullNameRefUser"))
                        .from(user)
                        .where(user.id.eq(transaction.refUserId)),
                transaction.goodsId.as("goodsId"),
                JPAExpressions.select(goods.name.as("goodsName"))
                        .from(goods)
                        .where(goods.id.eq(transaction.goodsId)),
                transaction.quantity.as("quantity"),
                transaction.destination.as("destination"),
                transaction.customerName.as("customerName"),
                transaction.transactionTime.as("transactionTime"),
                transaction.origin.as("origin"),
                new CaseBuilder()
                        .when(transaction.origin.eq(true)).then("Nhập hàng")
                        .otherwise("Xuất hàng").as("originDescription"),
                transaction.image.as("image"),
                transaction.createdAt.as("createdAt"),
                transaction.updatedAt.as("updatedAt")
        );
    }

    @Override
    @Modifying
    @Transactional
    public long updateTransaction(String id, UpdateTransactionDTO dto) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(transaction.id.eq(id))
                .and(transaction.deleted.eq(false));

        JPAUpdateClause updateClause = new JPAUpdateClause(entityManager, transaction);

        boolean isUpdated = false;

        if (dto.getRefUserId() != null) {
            updateClause.set(transaction.refUserId, dto.getRefUserId());
            isUpdated = true;
        }
        if (dto.getCustomerName() != null) {
            updateClause.set(transaction.customerName, dto.getCustomerName());
            isUpdated = true;
        }
        if (dto.getGoodsId() != null) {
            updateClause.set(transaction.goodsId, dto.getGoodsId());
            isUpdated = true;
        }
        if (dto.getQuantity() != null) {
            updateClause.set(transaction.quantity, dto.getQuantity());
            isUpdated = true;
        }
        if (dto.getDestination() != null) {
            updateClause.set(transaction.destination, dto.getDestination());
            isUpdated = true;
        }
        if (dto.getImage() != null) {
            updateClause.set(transaction.image, dto.getImage());
            isUpdated = true;
        }
        if (dto.getOrigin() != null) {
            updateClause.set(transaction.origin, dto.getOrigin());
            isUpdated = true;
        }
        if (dto.getTransactionTime() != null) {
            updateClause.set(transaction.transactionTime, dto.getTransactionTime());
            isUpdated = true;
        }

        if (isUpdated) {
            updateClause.set(transaction.updatedAt, new java.util.Date());
        } else {
            throw new EditNotAllowedException("No data fields are updated!!!");
        }

        return updateClause.where(builder).execute();
    }

    @Override
    public Optional<TransactionDTO> getTransactionsById(String id) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(transaction.id.eq(id))
                .and(transaction.deleted.eq(false));

        return Optional.ofNullable(
                query.from(transaction)
                .where(builder)
                .select(transactionProjection())
                .fetchOne()
        );
    }


    @Override
    public List<TransactionDTO> getTransactionByFilter(String warehouseId, Boolean origin, Timestamp fromDate, Timestamp toDate) {

        BooleanBuilder builder = new BooleanBuilder()
                .and(transaction.deleted.eq(false));

        if (warehouseId != null) {
            builder.and(goods.warehouseId.eq(warehouseId));
        }

        if (origin != null) {
            builder.and(transaction.origin.eq(origin));
        }

        if (fromDate != null && toDate != null) {
            builder.and(transaction.createdAt.between(fromDate, toDate));
        } else if (fromDate != null) {
            builder.and(transaction.createdAt.goe(fromDate));
        } else if (toDate != null) {
            builder.and(transaction.createdAt.loe(toDate));
        }

        return query.from(transaction)
                .leftJoin(goods).on(goods.id.eq(transaction.goodsId))
                .where(builder)
                .select(transactionProjection())
                .fetch();
    }

    @Override
    @Modifying
    @Transactional
    public long deleteTransaction(String id) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(transaction.id.eq(id))
                .and(transaction.deleted.eq(false));

        return query.update(transaction)
                .where(builder)
                .set(transaction.deleted, true)
                .execute();
    }

    @Override
    public Float getQuantityByOrigin(String goodsId, Boolean origin , YearMonth yearMonth) {
        Date startDate = Date.valueOf(LocalDate.now().atStartOfDay().toLocalDate());
        Date endDate = Date.valueOf(LocalDate.now().plusMonths(1).atStartOfDay().toLocalDate());

        if (yearMonth != null) {
            startDate = Date.valueOf(yearMonth.atDay(1).atStartOfDay().toLocalDate());
            endDate = Date.valueOf(yearMonth.atDay(yearMonth.lengthOfMonth()).atStartOfDay().toLocalDate());

        }

        BooleanBuilder builder = new BooleanBuilder()
                .and(transaction.createdAt.between(startDate, endDate))
                .and(transaction.origin.eq(origin))
                .and(transaction.goodsId.eq(goodsId));

        Float totalQuantity = query.from(transaction)
                .where(builder)
                .select(transaction.quantity.sum())  // sum() trả về BigDecimal
                .fetchOne();

        // Kiểm tra nếu totalQuantity là null, trả về 0f nếu không có kết quả
        return totalQuantity != null ? totalQuantity : 0f;
    }
}
