package com.project.logistic_management_2.repository.transaction;

import com.project.logistic_management_2.entity.*;
import com.project.logistic_management_2.repository.BaseRepo;
import com.querydsl.core.BooleanBuilder;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class TransactionRepoImpl extends BaseRepo implements TransactionRepoCustom {
    public TransactionRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Optional<Transaction> getTransactionsById(String id) {
        QTransaction qTransaction = QTransaction.transaction;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qTransaction.id.eq(id));
        Transaction transaction = query.from(qTransaction)
                .where(builder)
                .select(qTransaction)
                .fetchOne();
        return Optional.ofNullable(transaction);
    }


    @Override
    public List<Transaction> getTransactionByFilter(String wareHouseId, Boolean origin, Timestamp fromDate, Timestamp toDate) {
        QTransaction qTransaction = QTransaction.transaction;
        QGoods qGoods = QGoods.goods;

        BooleanBuilder builder = new BooleanBuilder();

        if (wareHouseId != null) {
            builder.and(qGoods.warehouseId.eq(wareHouseId));
        }

        if (origin != null) {
            builder.and(qTransaction.origin.eq(origin));
        }

        if (fromDate != null && toDate != null) {
            builder.and(qTransaction.createdAt.between(fromDate, toDate));
        } else if (fromDate != null) {
            builder.and(qTransaction.createdAt.goe(fromDate));
        } else if (toDate != null) {
            builder.and(qTransaction.createdAt.loe(toDate));
        }

        return query.from(qTransaction)
                .leftJoin(qGoods).on(qGoods.id.eq(qTransaction.goodsId))
                .where(builder)
                .select(qTransaction)
                .fetch();
    }

}
