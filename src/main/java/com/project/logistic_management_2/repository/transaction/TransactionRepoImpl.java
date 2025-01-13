package com.project.logistic_management_2.repository.transaction;

import com.project.logistic_management_2.dto.transaction.TransactionDTO;
import com.project.logistic_management_2.entity.transaction.Transaction;
import com.project.logistic_management_2.enums.Pagination;
import com.project.logistic_management_2.enums.transaction.TransactionType;
import com.project.logistic_management_2.exception.define.EditNotAllowedException;
import com.project.logistic_management_2.exception.define.InvalidFieldException;
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


import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.project.logistic_management_2.entity.goods.QGoods.goods;
import static com.project.logistic_management_2.entity.transaction.QTransaction.transaction;
import static com.project.logistic_management_2.entity.user.QUser.user;

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
                transaction.origin.as("origin"), //true f
                new CaseBuilder()
                        .when(transaction.origin.eq(true)).then(TransactionType.INBOUND_TRANSACTION.getTitle())
                        .otherwise(TransactionType.OUTBOUND_TRANSACTION.getTitle()).as("originDescription"),
                transaction.image.as("image"),
                transaction.createdAt.as("createdAt"),
                transaction.updatedAt.as("updatedAt")
        );
    }

    BooleanBuilder initGetOneBuilder(String id) {
        return new BooleanBuilder()
                .and(transaction.id.eq(id))
                .and(transaction.deleted.eq(false));
    }

    @Override
    @Modifying
    @Transactional
    public long updateTransaction(Transaction OldTransaction, String id, TransactionDTO dto) {
        BooleanBuilder builder = initGetOneBuilder(id);

        JPAUpdateClause updateClause = new JPAUpdateClause(entityManager, transaction);

        boolean isUpdated = false;
        boolean isChanged = false;

        if (dto.getRefUserId() != null) {
            if(!dto.getRefUserId().equals(OldTransaction.getRefUserId())){
                isChanged = true;
            }
            updateClause.set(transaction.refUserId, dto.getRefUserId());
            isUpdated = true;
        }
        if (dto.getCustomerName() != null) {
            if(!dto.getCustomerName().equals(OldTransaction.getCustomerName())){
                isChanged = true;
            }
            updateClause.set(transaction.customerName, dto.getCustomerName());
            isUpdated = true;
        }
        if (dto.getGoodsId() != null) {
            if(!dto.getGoodsId().equals(OldTransaction.getGoodsId())){
                isChanged = true;
            }
            updateClause.set(transaction.goodsId, dto.getGoodsId());
            isUpdated = true;
        }
        if (dto.getQuantity() != null) {
            if(!dto.getQuantity().equals(OldTransaction.getQuantity())){
                isChanged = true;
            }
            updateClause.set(transaction.quantity, dto.getQuantity());
            isUpdated = true;
        }
        if (dto.getDestination() != null) {
            if(!dto.getDestination().equals(OldTransaction.getDestination())){
                isChanged = true;
            }
            updateClause.set(transaction.destination, dto.getDestination());
            isUpdated = true;
        }
        if (dto.getImage() != null) {
            if(!dto.getImage().equals(OldTransaction.getImage())){
                isChanged = true;
            }
            updateClause.set(transaction.image, dto.getImage());
            isUpdated = true;
        }
        if (dto.getOrigin() != null) {
            if(!dto.getOrigin().getValue().equals(OldTransaction.getOrigin())){
                isChanged = true;
            }
            updateClause.set(transaction.origin, dto.getOrigin().getValue());
            isUpdated = true;
        }
        if (dto.getTransactionTime() != null) {
            if(!dto.getTransactionTime().equals(OldTransaction.getTransactionTime())){
                isChanged = true;
            }
            updateClause.set(transaction.transactionTime, dto.getTransactionTime());
            isUpdated = true;
        }

        if (isUpdated) {
            updateClause.set(transaction.updatedAt, new Date());
        } else {
            throw new InvalidFieldException("No data fields are updated!!!");
        }

        if(!isChanged) {
            throw new EditNotAllowedException("Data is not changed!!!");
        }

        return updateClause.where(builder).execute();
    }

    @Override
    public Optional<TransactionDTO> getTransactionsById(String id) {
        BooleanBuilder builder = initGetOneBuilder(id);

        return Optional.ofNullable(
                query.from(transaction)
                .where(builder)
                .select(transactionProjection())
                .fetchOne()
        );
    }

    @Override
    public List<TransactionDTO> getTransactionByFilter(int page, String warehouseId, Boolean origin, Date fromDate, Date toDate) {

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

        long offset = (long) (page - 1) * Pagination.TEN.getSize();

        return query.from(transaction)
                .leftJoin(goods).on(goods.id.eq(transaction.goodsId))
                .where(builder)
                .select(transactionProjection())
                .orderBy(transaction.updatedAt.desc())
                .offset(offset)
                .limit(Pagination.TEN.getSize())
                .fetch();
    }

    @Override
    @Modifying
    @Transactional
    public long deleteTransaction(String id) {
        BooleanBuilder builder = initGetOneBuilder(id);
        return query.update(transaction)
                .where(builder)
                .set(transaction.deleted, true)
                .execute();
    }

    @Override
    public Float getQuantityByOrigin(String goodsId, Boolean origin , Date fromDate, Date toDate) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(transaction.origin.eq(origin))
                .and(transaction.goodsId.eq(goodsId));
        if (fromDate != null && toDate != null) {
            builder.and(transaction.createdAt.between(fromDate, toDate));
        }

        Float totalQuantity = query.from(transaction)
                .where(builder)
                .select(transaction.quantity.sum())
                .fetchOne();

        return totalQuantity != null ? totalQuantity : 0f;
    }
}
